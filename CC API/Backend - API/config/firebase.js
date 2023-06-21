const firebase = require("firebase/app");
require("firebase/auth");
require("firebase/storage");
require('firebase/database');

// Add Firebase SDK Snippet
const firebaseConfig = {
------------------- Secret-------------------
};


firebase.initializeApp(firebaseConfig);
const storage = firebase.storage();
const database = firebase.database();

module.exports = {firebase, database, storage};
