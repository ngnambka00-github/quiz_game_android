from app import app
from flaskext.mysql import MySQL

mysql = MySQL()
app.config["MYSQL_DATABASE_USER"] = "root"
app.config["MYSQL_DATABASE_PASSWORD"] = "admin"
app.config["MYSQL_DATABASE_DB"] = "quiz_game"
app.config["MYSQL_DATABASE_HOST"] = "localhost"
mysql.init_app(app)

UPLOAD_FOLDER = 'Server/static/avatar_image/'
app.secret_key = "secret key"
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER
app.config['MAX_CONTENT_LENGTH'] = 16 * 1024 * 1024