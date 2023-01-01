from app import app
from flaskext.mysql import MySQL
from flask_mail import Mail

mysql = MySQL()
app.config["MYSQL_DATABASE_USER"] = "root"
app.config["MYSQL_DATABASE_PASSWORD"] = "root"
app.config["MYSQL_DATABASE_DB"] = "quiz_game"
app.config["MYSQL_DATABASE_HOST"] = "localhost"
mysql.init_app(app)

mail = Mail()
app.config['MAIL_SERVER'] = 'smtp.gmail.com'
app.config['MAIL_PORT'] = 465
app.config['MAIL_USE_TLS'] = False
app.config['MAIL_USE_SSL'] = True
app.config['MAIL_USERNAME'] = 'quizzgame123@gmail.com'
app.config['MAIL_PASSWORD'] = 'xeqrygrkyiioldbp'
# 'xeqrygrkyiioldbp'

# 'uirvoiicajstinox'
mail.init_app(app)