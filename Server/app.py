from flask import Flask
from flask_cors import CORS, cross_origin

app = Flask(__name__)
CORS(app)
app.config['CORS_HEADERS'] = 'Content-Type'
app.config["UPLOADED_PHOTOS_DEST"] = "static/avatar"
app.config["SECRET_KEY"] = "SECRET_KEY_ABCD"
