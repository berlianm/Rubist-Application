const express = require("express");
const router = express.Router();

const {
  register,
  login,
  forgetPassword,
  signOut
} = require("../controllers/auth");

router.post("/register", register);

router.post("/login", login);

router.post("/signOut", signOut);

router.post("/forget-password", forgetPassword);

module.exports = router;
