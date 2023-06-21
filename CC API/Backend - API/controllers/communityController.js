const { firebase, database } = require('../config/firebase');
const { Storage } = require("@google-cloud/storage");

const storage = new Storage({
  projectId: "c-project-387808",
  keyFilename: "credential.json"
});

class CommunityController {
  static createPost(req, res) {
    const { title, content, userId } = req.body;
  
    if (!title || !content || !userId) {
      return res.status(400).json({ error: 'Title, content, and userId are required' });
    }
  
    // Memeriksa apakah userId yang diberikan sesuai dengan userId yang ada di Firebase Database
    firebase
      .database()
      .ref('users/' + userId)
      .once('value')
      .then((snapshot) => {
        const userData = snapshot.val();
  
        if (!userData) {
          // UserId yang diberikan tidak cocok dengan userId yang ada di Firebase Database
          return res.status(400).json({ error: 'Invalid userId' });
        }
  
        // Membuat objek data postingan
        const postData = {
            title,
            content,
            userId,
            createdAt: new Date().toLocaleString(userData.timezone, {
              year: 'numeric',
              month: 'long',
              day: 'numeric',
              hour: 'numeric',
              minute: 'numeric',
            }).replace(/, (\d+)/, ' $1 |'),
          };        
  
  
        const newPostRef = firebase.database().ref('posts').push();
  
        const photo = req.file;
  
        if (photo) {
          const bucketName = "c-project-387808-postsimage";
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
                postId: newPostRef.key
              };
  
              res.json(responseData);
            })
            .catch((error) => {
              console.error(error);
              res.status(500).json({ error: 'Failed to create post' });
            });
        }
      })
      .catch((error) => {
        console.error(error);
        res.status(500).json({ error: 'Failed to create post' });
      });
  }
  
  
  static getPostById(req, res) {
    const postId = req.params.postId;
  
    database.ref(`posts/${postId}`)
      .once('value')
      .then(async (snapshot) => {
        const post = snapshot.val();
        if (post) {
          const userId = post.userId;
          const userSnapshot = await database.ref(`users/${userId}`).once('value');
          const user = userSnapshot.val();
          
          if (user) {
            const postData = {
              id: postId,
              ...post,
              userId: userId,
              displayName: user.name,
              ProfileUrl: user.photoURL
            };
  
            if (post.photoUrl) {
              postData.photoUrl = post.photoUrl;
            }
  
            res.json({ postData });
          } else {
            res.status(404).json({ error: 'User not found' });
          }
        } else {
          res.status(404).json({ error: 'Post not found' });
        }
      })
      .catch((error) => {
        console.error(error);
        res.status(500).json({ error: 'Failed to get post data' });
      });
  }
  
  
  static createComment(req, res) {
    const { postId, content, userId } = req.body;
  
    if (!content || !userId) {
      return res.status(400).json({ error: 'Content and userId are required' });
    }
  
    // Memeriksa apakah userId yang diberikan sesuai dengan userId yang ada di Firebase Database
    firebase
      .database()
      .ref('users/' + userId)
      .once('value')
      .then((snapshot) => {
        const userData = snapshot.val();
  
        if (!userData) {
          // UserId yang diberikan tidak cocok dengan userId yang ada di Firebase Database
          return res.status(400).json({ error: 'Invalid userId' });
        }
  
        const newCommentRef = database.ref(`comments/${postId}`).push();
        const commentId = newCommentRef.key;
  
        const commentData = {
          content,
          commentId,
          userId,
          createdAt: new Date().toLocaleString(userData.timezone, {
            year: 'numeric',
            month: 'long',
            day: 'numeric',
            hour: 'numeric',
            minute: 'numeric',
          }).replace(/, (\d+)/, ' $1 |'),
        };   
        
        newCommentRef
          .set(commentData)
          .then(() => res.json({ commentId }))
          .catch((error) => {
            console.error(error);
            res.status(500).json({ error: 'Failed to create comment' });
          });
      })
      .catch((error) => {
        console.error(error);
        res.status(500).json({ error: 'Failed to create comment' });
      });
  }
  
static getAllPosts(req, res) {
  database.ref('posts')
    .once('value')
    .then(async (snapshot) => {
      const posts = snapshot.val();
      if (posts) {
        const postIds = Object.keys(posts);
        const promises = postIds.map(async (postId) => {
          const post = posts[postId];
          const userId = post.hasOwnProperty('userId') ? post.userId : null;
          if (userId) {
            const userSnapshot = await database.ref(`users/${userId}`).once('value');
            const user = userSnapshot.val();
            if (user) {
              const postData = {
                id: postId,
                ...post,
                userId: userId,
                displayName: user.name,
                ProfileUrl: user.photoURL
              };
              if (post.photoUrl) {
                const bucketName = "c-project-387808-postsimage";
                const bucket = storage.bucket(bucketName);
                const file = bucket.file(`${postId}_photo.jpg`);
                const [metadata] = await file.getMetadata();
                const url = metadata.mediaLink;
                postData.photoUrl = url;
              }
              return postData;
            }
          }
          return null;
        });

        Promise.all(promises)
          .then((postDatas) => {
            const filteredPostDatas = postDatas.filter((postData) => postData !== null);
            // Mengurutkan postingan dari terbaru ke terlama berdasarkan createdAt
            const sortedPostDatas = filteredPostDatas.sort((a, b) => b.createdAt.localeCompare(a.createdAt));
            res.json({ listPostData: sortedPostDatas });
          })
          .catch((error) => {
            console.error(error);
            res.status(500).json({ error: 'Failed to get posts' });
          });
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
        const commentIds = Object.keys(comments);

        // Array untuk menyimpan promise pengambilan data pengguna
        const userPromises = [];

        // Loop melalui setiap komentar
        const commentsWithUserData = commentIds.map((commentId) => {
          const comment = comments[commentId];

          // Dapatkan promise pengambilan data pengguna
          const userPromise = database.ref(`users/${comment.userId}`)
            .once('value')
            .then((userSnapshot) => {
              const user = userSnapshot.val();
              if (user) {
                comment.displayName = user.name;
                comment.photoUrl = user.photoURL;
              }
              return comment;
            });

          userPromises.push(userPromise);
          return userPromise;
        });

        // Tunggu semua promise selesai dijalankan
        Promise.all(commentsWithUserData)
          .then((commentsData) => {
            // Mengurutkan komentar dari terbaru ke terlama berdasarkan createdAt
            const sortedCommentsData = commentsData.sort((a, b) => b.createdAt.localeCompare(a.createdAt));
            res.json({ commentData: sortedCommentsData });
          })
          .catch((error) => {
            console.error(error);
            res.status(500).json({ error: 'Failed to get comments' });
          });
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
