const express = require("express");
const router = express.Router();

const {
  register,
  login,
  forgetPassword,
  signOut,
  getData,
  getDataByUid
} = require("../controllers/auth");

router.post("/register", register);

router.post("/login", login);

router.post("/signOut", signOut);

router.post("/forget-password", forgetPassword);

router.get("/getData", getData);

router.get("/getData/:uid", getDataByUid);

module.exports = router;
