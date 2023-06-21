const { firebase } = require("./../config/firebase");

exports.register = (req, res) => {
  if (!req.body.email || !req.body.password || !req.body.name) {
    return res.status(422).json({
      name: "name is required",
      email: "email is required",
      password: "password is required",
    });
  }

  const isGoogleEmail = req.body.email.endsWith("@gmail.com") || req.body.email.endsWith("@googlemail.com");
  if (!isGoogleEmail) {
    return res.status(422).json({
      email: "Only Google emails are allowed",
    });
  }

  // Create user in Firebase Authentication
  firebase
    .auth()
    .createUserWithEmailAndPassword(req.body.email, req.body.password)
    .then((userData) => {
      const uid = userData.user.uid;
      const name = req.body.name;
      const email = req.body.email;

      // Update displayName in Firebase Authentication
      firebase
        .auth()
        .currentUser.updateProfile({
          displayName: name,
        })
        .then(() => {
          // Send email verification
          firebase
            .auth()
            .currentUser.sendEmailVerification()
            .then(() => {
              return res.status(201).json(userData);
            })
            .catch((error) => {
              return res.status(500).json({ error: error.message });
            });
        })
        .catch((error) => {
          return res.status(500).json({ error: error.message });
        });
    })
    .catch((error) => {
      let errorCode = error.code;
      let errorMessage = error.message;
      if (errorCode == "auth/weak-password") {
        return res.status(500).json({ error: errorMessage });
      } else {
        return res.status(500).json({ error: errorMessage });
      }
    });
};

exports.login = (req, res) => {
  if (!req.body.email || !req.body.password) {
    return res.status(422).json({
      email: "email is required",
      password: "password is required",
    });
  }

  firebase
    .auth()
    .signInWithEmailAndPassword(req.body.email, req.body.password)
    .then((userCredential) => {
      const user = userCredential.user;
      if (!user.emailVerified) {
        return res.status(401).json({ error: "Email not verified. Please verify your email first." });
      }

      const uid = user.uid;
      const name = user.displayName;
      const email = user.email;

      firebase
      .database()
      .ref("users/" + uid)
      .once("value")
      .then((snapshot) => {
        if (snapshot.exists()) {
          // Data pengguna sudah ada, lakukan pembaruan data
          return snapshot.ref.update({
            name: name,
            email: email,
          });
        } else {
          // Data pengguna belum ada, simpan data baru
          return snapshot.ref.set({
            name: name,
            email: email,
            uid: uid,
          });
        }
      })
      .then(() => {
        return res.status(200).json(user);
      })
      .catch((error) => {
        return res.status(500).json({ error: error.message });
      });
  })
  .catch((error) => {
    let errorCode = error.code;
    let errorMessage = error.message;
    if (errorCode === "auth/wrong-password") {
      return res.status(500).json({ error: errorMessage });
    } else {
      return res.status(500).json({ error: errorMessage });
    }
  });
};


exports.forgetPassword = (req, res) => {
  if (!req.body.email) {
    return res.status(422).json({ email: "email is required" });
  }
  firebase
    .auth()
    .sendPasswordResetEmail(req.body.email)
    .then(function () {
      return res.status(200).json({ status: "Password Reset Email Sent" });
    })
    .catch(function (error) {
      let errorCode = error.code;
      let errorMessage = error.message;
      if (errorCode == "auth/invalid-email") {
        return res.status(500).json({ error: errorMessage });
      } else if (errorCode == "auth/user-not-found") {
        return res.status(500).json({ error: errorMessage });
      }
    });
};

exports.signOut = (req, res) => {
  firebase
    .auth()
    .signOut()
    .then(() => {
      return res.status(200).json({ message: "User signed out successfully" });
    })
    .catch((error) => {
      return res.status(500).json({ error: error.message });
    });
};

exports.getRegisteredUid = () => {
  // Pastikan pengguna sudah login sebelumnya
  const user = firebase.auth().currentUser;
  if (user) {
    const uid = user.uid;
    return uid; // Mengembalikan UID pengguna
  } else {
    console.log("Pengguna belum login.");
    return null; // Mengembalikan null jika pengguna belum login
  }
};

exports.getData = (req, res) => {
  firebase
    .database()
    .ref("users")
    .once("value")
    .then((snapshot) => {
      const data = snapshot.val();
      if (data) {
        res.json(data);
      } else {
        res.status(404).json({ error: "No data found" });
      }
    })
    .catch((error) => {
      console.error(error);
      res.status(500).json({ error: "Failed to get data" });
    });
};

exports.getDataByUid = (req, res) => {
  const uid = req.params.uid;

  firebase
    .database()
    .ref("users/" + uid)
    .once("value")
    .then((snapshot) => {
      const data = snapshot.val();
      if (data) {
        res.json(data);
      } else {
        res.status(404).json({ error: "Data not found" });
      }
    })
    .catch((error) => {
      console.error(error);
      res.status(500).json({ error: "Failed to get data" });
    });
};
