from flask import Flask, request, jsonify
import json
import os
import numpy as np
import tensorflow as tf
from tensorflow import keras
import tensorflow_hub as hub
from collections import OrderedDict
from PIL import Image
from keras.utils import custom_object_scope


app = Flask(__name__)

def custom_objects():
    return {"KerasLayer": hub.KerasLayer}

@app.route("/uploadImage", methods=['POST'])
def trash_classifier_():
    try:
        # Ambil gambar
        image_request = request.files['image']

        # Konversi gambar menjadi array
        image_pil = Image.open(image_request).convert("RGB")

        # Ngeresize gambar
        expected_size = (224, 224)
        resize_image_pil = image_pil.resize(expected_size)

        # Generate array dengan numpy
        image_array = np.array(resize_image_pil)
        rescaled_image_array = image_array / 255.0
        batched_rescaled_image_array = np.array([rescaled_image_array])
        print(batched_rescaled_image_array.shape)

        # Load model
        with custom_object_scope(custom_objects()):
            loaded_model = tf.keras.models.load_model("mobilenet_model.h5")
        
        # print(loaded_model.get_config())
        result = loaded_model.predict(batched_rescaled_image_array)
        
        # Get predicted class
        predicted_class = get_formatted_predict_result(result)
        
        # Baca data dari file JSON
        with open('sampah_data.json') as file:
            sampah_data = json.load(file)
        
        # Array kosong untuk menyimpan data terurut
        data_terurut = []

        # Iterasi setiap jenis sampah dalam data saat ini
        for jenis_sampah, data in sampah_data.items():
            # Objek dengan properti yang sesuai
            objek = OrderedDict()
            objek["Jenis Sampah"] = jenis_sampah
            objek["Deskripsi"] = data["Deskripsi"]
            objek["Daur Ulang"] = data["Daur Ulang"]
            objek["Pembuangan"] = data["Pembuangan"]
            objek["Cara Daur Ulang"] = data["Cara Daur Ulang"]
            objek["Dampak Lingkungan"] = data["Dampak Lingkungan"]
            
            # Objek ditambahkan ke dalam array terurut
            data_terurut.append(objek) 

            # Kembalikan respons dalam format JSON
            if jenis_sampah == predicted_class:
                return jsonify(objek), 200
                
        return jsonify({'error': 'Deskripsi tidak tersedia.'}), 404        

    except Exception as e:
        return str(e), 500

def get_formatted_predict_result(predict_result):
    class_indices = {'cardboard': 0, 'glass': 1, 'metal': 2, 'paper': 3, 'plastic': 4, 'trash': 5}
    inverted_class_indices = {}

    for key in class_indices :
        class_indices_key = key
        class_indices_value = class_indices[key]

        inverted_class_indices[class_indices_value] = class_indices_key

    processed_predict_result = predict_result[0]
    maxIndex = 0
    maxValue = 0

    for index in range(len(processed_predict_result)):
        if processed_predict_result[index] > maxValue:
            maxValue = processed_predict_result[index]
            maxIndex = index

    return inverted_class_indices[maxIndex]


if __name__ == "__main__":
    app.run(debug=True, host="0.0.0.0", port=int(os.environ.get("PORT", 8080)))