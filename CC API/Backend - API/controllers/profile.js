const { Storage } = require("@google-cloud/storage");
const { firebase } = require('../config/firebase');

const storage = new Storage({
  projectId: "c-project-387808",
  keyFilename: "credential.json"
});

const updateProfile = async (req, res) => {
  try {
    const { userId, mobilePhone, location } = req.body;
    const photo = req.file;

    if (!userId) {
      return res.status(400).json({ error: 'userId is required' });
    }

    // Memeriksa apakah userId yang diberikan sesuai dengan userId yang ada di Firebase Database
    firebase
      .database()
      .ref('users/' + userId)
      .once('value')
      .then(async (snapshot) => {
        const userData = snapshot.val();

        if (!userData) {
          return res.status(400).json({ error: 'Invalid userId' });
        }

        const profileData = {};

        if (mobilePhone) {
          profileData.mobilePhone = mobilePhone;
        }
        if (location) {
          profileData.location = location;
        }
        if (!photo) {
          profileData.photoURL = null;
          await addProfileData(userId, profileData);
          return res.status(200).json({ message: 'Profile updated successfully', photoURL: profileData.photoURL });
        }

        const bucketName = "gs://c-project-387808-profileimage";
        const bucket = storage.bucket(bucketName);
        const newFileName = `${userId}_photo.jpg`;
        const fileUpload = bucket.file(newFileName);

        const blobStream = fileUpload.createWriteStream({
          metadata: {
            contentType: photo.mimetype,
          },
        });

        blobStream.on('error', (error) => {
          console.error(error);
          res.status(500).json({ error: 'Failed to upload photo' });
        });

        blobStream.on('finish', async () => {
          try {
            const [metadata] = await fileUpload.getMetadata();
            const url = metadata.mediaLink;

            profileData.photoURL = url;

            await addProfileData(userId, profileData);

            res.status(200).json({ message: 'Profile updated successfully', photoURL: profileData.photoURL });
        
          } catch (error) {
            console.error(error);
            res.status(500).json({ error: 'Failed to update profile' });
          }
        });

        blobStream.end(photo.buffer);
      });
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: 'Failed to update profile' });
  }
};

const addProfileData = async (uid, profileData) => {
  // Memperbarui data profil pengguna di Firebase Realtime Database
  await firebase
    .database()
    .ref("users/" + uid)
    .update(profileData); // Gunakan metode `update` untuk memperbarui data
};

module.exports = {
  updateProfile,
};
