from app import app
from flaskext.mysql import MySQL
import os
from dotenv import load_dotenv
from flask_mail import Mail

load_dotenv()

# config http app
app.config["CORS_HEADERS"] = os.getenv("CORS_HEADERS")
app.config["UPLOADED_PHOTOS_DEST"] = os.getenv("UPLOADED_PHOTOS_DEST")
app.config["SECRET_KEY"] = os.getenv("SECRET_KEY")

# config for email
mail = Mail()
app.config['MAIL_SERVER'] = 'smtp.gmail.com'
app.config['MAIL_PORT'] = 465
app.config['MAIL_USE_TLS'] = False
app.config['MAIL_USE_SSL'] = True
app.config['MAIL_USERNAME'] = 'quizzgame123@gmail.com'
app.config['MAIL_PASSWORD'] = 'xeqrygrkyiioldbp'
mail.init_app(app)

# config MYSQL
mysql = MySQL()
app.config["MYSQL_DATABASE_HOST"] = os.getenv("MYSQL_DATABASE_HOST")
app.config["MYSQL_DATABASE_USER"] = os.getenv("MYSQL_DATABASE_USER")
app.config["MYSQL_DATABASE_PASSWORD"] = os.getenv("MYSQL_DATABASE_PASSWORD")
app.config["MYSQL_DATABASE_DB"] = os.getenv("MYSQL_DATABASE_DB")
mysql.init_app(app)

# print(f"{type(bool(os.getenv('MAIL_USE_TLS')))}")
