const { firebase, database } = require('../config/firebase');
const { Storage } = require("@google-cloud/storage");

const storage = new Storage({
  projectId: "c-project-387808",
  keyFilename: "credential.json"
});

class CommunityController {
  static createPost(req, res) {
    const { title, content, photo } = req.body;
    const currentUser = firebase.auth().currentUser;
    const userId = currentUser ? currentUser.uid : null; // Mendapatkan ID pengguna saat ini
  
    if (!title || !content) {
      return res.status(400).json({ error: 'Title and content are required' });
    }
  
    const newPostRef = database.ref('posts').push();
  
    const postData = {
      title,
      content,
      photoUrl: null,
      userId: userId,
    };
  
    if (photo) {
      const bucketName = 'c-project-387808.appspot.com';
      const bucket = storage.bucket(bucketName);
      const file = bucket.file(`${newPostRef.key}_photo.jpg`);
  
      const blobStream = file.createWriteStream({
        metadata: {
          contentType: photo.mimetype,
        },
      });
  
      blobStream.on('error', (error) => {
        console.error(error);
        res.status(500).json({ error: 'Failed to upload photo' });
      });
  
      blobStream.on('finish', async () => {
        const [metadata] = await file.getMetadata();
        const url = metadata.mediaLink;
  
        postData.photoUrl = url;
        postData.postId = newPostRef.key; // Menambahkan postId ke postData
  
        newPostRef.set(postData); // Menyimpan postData ke Firebase Database
  
        const responseData = {
          postId: newPostRef.key,
        };
  
        res.json(responseData);
      });
  
      blobStream.end(photo.buffer);
    } else {
      newPostRef
        .set(postData)
        .then(() => {
          const responseData = {
            postId: newPostRef.key,
          };
  
          res.json(responseData);
        })
        .catch((error) => {
          console.error(error);
          res.status(500).json({ error: 'Failed to create post' });
        });
    }
  }
  
 static getPostById(req, res) {
  const { postId } = req.params;

  const postRef = database.ref(`posts/${postId}`);
  postRef.once('value', async (snapshot) => {
    const post = snapshot.val();
    if (post) {
      if (post.photoUrl) {
        const photoUrl = post.photoUrl;
        const bucketName = 'c-project-387808.appspot.com'; // Nama bucket Firebase Storage Anda
        const bucket = storage.bucket(bucketName);
        const file = bucket.file(`${postId}_photo.jpg`);

        // Mendapatkan URL foto dari Firebase Storage
        const [metadata] = await file.getMetadata();
        const url = metadata.mediaLink;
        post.photoUrl = url;
      }
      res.json(post);
    } else {
      res.status(404).json({ error: 'Post not found' });
    }
  }, (error) => {
    console.error(error);
    res.status(500).json({ error: 'Failed to fetch post' });
  });
}

static createComment(req, res) {
  const { postId, content } = req.body;
  const currentUser = firebase.auth().currentUser;
  const userId = currentUser ? currentUser.uid : null;

  if (!content) {
    return res.status(400).json({ error: 'Content is required' });
  }

  const newCommentRef = database.ref(`comments/${postId}`).push();
  const commentId = newCommentRef.key;

  const commentData = {
    content,
    commentId,
    userId,
  };

  newCommentRef
    .set(commentData)
    .then(() => res.json({ commentId }))
    .catch((error) => {
      console.error(error);
      res.status(500).json({ error: 'Failed to create comment' });
    });
}




  static getAllPosts(req, res) {
    database.ref('posts')
      .once('value')
      .then((snapshot) => {
        const posts = snapshot.val();
        if (posts) {
          res.json(posts);
        } else {
          res.status(404).json({ error: 'No posts found' });
        }
      })
      .catch((error) => {
        console.error(error);
        res.status(500).json({ error: 'Failed to get posts' });
      });
  }

  static getAllComments(req, res) {
    const postId = req.params.postId;

    database.ref(`comments/${postId}`)
      .once('value')
      .then((snapshot) => {
        const comments = snapshot.val();
        if (comments) {
          res.json(comments);
        } else {
          res.status(404).json({ error: 'No comments found' });
        }
      })
      .catch((error) => {
        console.error(error);
        res.status(500).json({ error: 'Failed to get comments' });
      });
  }
}

module.exports = CommunityController;
