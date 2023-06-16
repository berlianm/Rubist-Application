const express = require("express");
const router = express.Router();
const multer = require("multer");
const Profile = require("../controllers/profile");

const multerUpload = multer({
  storage: multer.memoryStorage(),
  limits: {
    fileSize: 5 * 1024 * 1024, // no larger than 5mb, you can change as needed.
  },
});

router.post("/updateProfile", multerUpload.single("file"), (req, res) => {
  Profile.updateProfile(req, res);
});

module.exports = router;
