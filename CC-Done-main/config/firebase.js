const firebase = require("firebase/app");
require("firebase/auth");
require("firebase/storage");
require('firebase/database');

// Add Firebase SDK Snippet
const firebaseConfig = {
  apiKey: "AIzaSyAmTvMWmj_NtbzeU5JGVke08VcPgoMoxAM",
  authDomain: "c-project-387808.firebaseapp.com",
  projectId: "c-project-387808",
  databaseURL : "https://c-project-387808-default-rtdb.asia-southeast1.firebasedatabase.app",
  storageBucket: "c-project-387808.appspot.com",
  messagingSenderId: "66950882895",
  appId: "1:66950882895:web:cb20b79f2ec4e25606ac3a"
};


firebase.initializeApp(firebaseConfig);
const storage = firebase.storage();
const database = firebase.database();

module.exports = {firebase, database, storage};
