ML Model API

Cloud Run Deploy
docker build -t rubist-ml-api
docker tag rubist-ml-api gcr.io/c-project-387808/rubist-ml-api
docker push gcr.io/c-project-387808/rubist-ml-api
gcloud run deploy rubist   --image gcr.io/c-project-387808/rubist-ml-api --platform managed   --region asia-southeast2   --allow-unauthenticated
