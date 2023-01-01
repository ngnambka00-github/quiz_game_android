from flask import Flask
from flask_cors import CORS
from router.home import home_api

app = Flask(__name__)
CORS(app)

app.register_blueprint(home_api, url_prefix="/")
