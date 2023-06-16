const { Storage } = require("@google-cloud/storage");
const { firebase } = require('../config/firebase');

const storage = new Storage({
  projectId: "c-project-387808",
  keyFilename: "credential.json"
});

const addProfileData = (uid, mobilePhone, location) => {
  const profileData = {};
  if (mobilePhone) {
    profileData.mobilePhone = mobilePhone;
  }
  if (location) {
    profileData.location = location;
  }
  return firebase
    .database()
    .ref("users/" + uid)
    .update(profileData);
};

const bucketName = "gs://c-project-387808-profileimage";
const bucket = storage.bucket(bucketName);

const uploadImageToStorage = (file) => {
  return new Promise((resolve, reject) => {
    if (!file) {
      reject("No image file");
    }
    let newFileName = `${file.originalname}_${Date.now()}`;

    let fileUpload = bucket.file(newFileName);

    const blobStream = fileUpload.createWriteStream({
      metadata: {
        contentType: file.mimetype,
      },
    });

    blobStream.on("error", (error) => {
      reject(error);
    });

    blobStream.on("finish", () => {
      // The public URL can be used to directly access the file via HTTP.
      const url = `https://storage.googleapis.com/${bucket.name}/${fileUpload.name}`;
      resolve(url);
    });

    blobStream.end(file.buffer);
  });
};

const updateProfile = async (req, res) => {
  try {
    const file = req.file;
    const mobilePhone = req.body.mobilePhone;
    const location = req.body.location;

    const currentUser = firebase.auth().currentUser;

    if (currentUser) {
      const uid = currentUser.uid;

      const url = file ? await uploadImageToStorage(file) : null;

      await addProfileData(uid, mobilePhone, location);

      // Update phone number in Firebase Authentication
      await currentUser.updateProfile({
        phoneNumber: mobilePhone,
        photoURL: url // Set the photo URL as the user's profile photo
      });

      return res.status(200).send({
        image: url,
      });
    } else {
      return res.status(401).send({
        error: 'User not authenticated',
      });
    }
  } catch (error) {
    return res.status(500).send({
      error: error.message,
    });
  }
};

module.exports = {
  updateProfile
};
