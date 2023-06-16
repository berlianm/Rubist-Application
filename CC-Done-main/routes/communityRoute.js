const express = require('express');
const router = express.Router();
const multer = require('multer');
const CommunityController = require('../controllers/communityController');

const multerUpload = multer({
  storage: multer.memoryStorage(),
  limits: {
    fileSize: 5 * 1024 * 1024, // no larger than 5mb, you can change as needed.
  },
});

router.post('/posts', multerUpload.single('photo'), CommunityController.createPost);
router.get('/posts', CommunityController.getAllPosts);
router.get('/posts/:postId', CommunityController.getPostById);
router.post('/comments', CommunityController.createComment);
router.get('/posts/:postId/comments', CommunityController.getAllComments);

module.exports = router;
