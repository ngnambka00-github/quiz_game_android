import pymysql
from app import app
from config import mysql, mail
from flask import jsonify
from flask import request, flash, url_for, redirect
import constants
from flask_cors import cross_origin
from flask_uploads import IMAGES, UploadSet, configure_uploads
from flask_mail import Message
import bcrypt

photos = UploadSet("photos", IMAGES)
configure_uploads(app, photos)


@app.route("/send_mail", methods=["POST"], )
def send_mail():
    data = request.get_json()
    recipients = data.get('to_mail', '')
    subject = data.get('subject', '')
    body_message = data.get('body', '')

    msg = Message(subject=subject, sender='testId@gmail.com', recipients=[recipients])
    msg.body = body_message
    mail.send(msg)
    respone = jsonify('Sent Email Successfully!')
    respone.status_code = 200
    return respone


# --------------------- USER ------------------------
@app.route('/user', methods=["POST"])
def create_user():
    """
        Tạo User mới
        :return:
        """
    try:
        obj_json = request.json
        fullname = obj_json['fullname']
        email = obj_json['email']
        password = obj_json['password']

        if fullname and email and password and request.method == 'POST':
            conn = mysql.connect()
            cursor = conn.cursor(pymysql.cursors.DictCursor)
            cursor.execute(constants.QUERY_INSERT_USER, (fullname, email, password))
            conn.commit()

            respone = jsonify('User added successfully!')
            respone.status_code = 200
            return respone
        else:
            return showMessage()
    except Exception as e:
        print(e)
    finally:
        cursor.close()
        conn.close()


@app.route('/user', methods=["GET"])
def get_users():
    """
        Lấy tất cả danh sách users
        :return:
        """
    try:
        conn = mysql.connect()
        cursor = conn.cursor(pymysql.cursors.DictCursor)
        cursor.execute(constants.QUERY_GET_USERS)
        userRows = cursor.fetchall()

        respone = jsonify(userRows)
        respone.status_code = 200
        return respone
    except Exception as e:
        print(e)
    finally:
        cursor.close()
        conn.close()


@app.route('/user/<int:user_id>', methods=["GET"])
def get_user_by_id(user_id):
    """
        Lấy ra 1 user theo user_id
        :param user_id:
        :return:
        """
    try:
        conn = mysql.connect()
        cursor = conn.cursor(pymysql.cursors.DictCursor)
        cursor.execute(constants.QUERY_GET_USER_BY_ID, user_id)
        userRow = cursor.fetchone()

        respone = jsonify(userRow)
        respone.status_code = 200
        return respone
    except Exception as e:
        print(e)
    finally:
        cursor.close()
        conn.close()


@app.route('/user', methods=['PUT'])
def update_user():
    try:
        obj_json = request.json
        user_id = obj_json['user_id']
        fullname = obj_json['fullname']
        email = obj_json['email']
        password = obj_json['password']
        coin = obj_json['coin']
        image_path = obj_json["image_path"]

        if fullname and email and password and coin and user_id and request.method == 'PUT':
            bind_data = (email, password, coin, fullname, image_path, user_id)
            conn = mysql.connect()
            cursor = conn.cursor()
            print(f"[CHECK CURSOR]: {cursor}")
            cursor.execute(constants.QUERY_UPDATE_USER, bind_data)
            conn.commit()

            respone = jsonify('User updated successfully!')
            respone.status_code = 200
            return respone
        else:
            return showMessage()
    except Exception as e:
        print(e)
    finally:
        cursor.close()
        conn.close()


@app.route('/user/<int:user_id>', methods=['DELETE'])
def delete_user(user_id):
    """
        Xóa 1 user nào đó
        :param user_id:
        :return:
        """
    try:
        conn = mysql.connect()
        cursor = conn.cursor()
        cursor.execute(constants.QUERY_DELETE_USER, (user_id,))
        conn.commit()

        respone = jsonify('Employee deleted successfully!')
        respone.status_code = 200
        return respone
    except Exception as e:
        print(e)
    finally:
        cursor.close()
        conn.close()


# ----------------------------------------------------


# --------------------- CATEGORY ------------------------
@app.route('/category', methods=["POST"])
def create_category():
    """
        Tạo Danh mục mới
        :return:
        """
    try:
        obj_json = request.json
        category_name = obj_json['category_name']
        category_image = obj_json['category_image']

        if category_name and category_name and request.method == 'POST':
            conn = mysql.connect()
            cursor = conn.cursor(pymysql.cursors.DictCursor)
            cursor.execute(constants.QUERY_INSERT_CATEGORY, (category_name, category_image))
            conn.commit()

            respone = jsonify('Category added successfully!')
            respone.status_code = 200
            return respone
        else:
            return showMessage()
    except Exception as e:
        print(e)
    finally:
        cursor.close()
        conn.close()


@app.route('/category', methods=["GET"])
def get_categories():
    """
    Lấy tất cả danh mục câu hỏi
    :return:
    """
    try:
        conn = mysql.connect()
        cursor = conn.cursor(pymysql.cursors.DictCursor)
        cursor.execute(constants.QUERY_GET_CATEGORIES)
        categoryRows = cursor.fetchall()

        respone = jsonify(categoryRows)
        respone.status_code = 200
        return respone
    except Exception as e:
        print(e)
    finally:
        cursor.close()
        conn.close()


# ----------------------------------------------------


# --------------------- QUESTION ------------------------
@app.route('/question', methods=["POST"])
def create_question():
    """
        Tạo Question mới
        :return:
        """
    try:
        obj_json = request.json
        question = obj_json['question']
        option1 = obj_json['option1']
        option2 = obj_json["option2"]
        option3 = obj_json["option3"]
        option4 = obj_json["option4"]
        answer = obj_json["answer"]
        category_id = obj_json["category_id"]
        explanation = ""
        image_url = ""

        if question and option1 and option2 and option3 and option4 and answer \
                and category_id and request.method == 'POST':
            conn = mysql.connect()
            cursor = conn.cursor(pymysql.cursors.DictCursor)
            cursor.execute(constants.QUERY_INSERT_QUESTION,
                           (question, option1, option2, option3, option4, answer, category_id))
            conn.commit()

            respone = jsonify('Question added successfully!')
            respone.status_code = 200
            return respone
        else:
            return showMessage()
    except Exception as e:
        print(e)
    finally:
        cursor.close()
        conn.close()


@app.route('/question/<int:category_id>', methods=["GET"])
def get_questions_by_category(category_id):
    """
    Lấy danh sách câu hỏi theo category
    :return:
    """
    try:
        conn = mysql.connect()
        cursor = conn.cursor(pymysql.cursors.DictCursor)
        cursor.execute(constants.QUERY_GET_QUESTION_BY_CATEGORY, category_id)
        questionsRow = cursor.fetchall()

        respone = jsonify(questionsRow)
        respone.status_code = 200
        return respone
    except Exception as e:
        print(e)
    finally:
        cursor.close()
        conn.close()


@app.route('/question/<int:category_id>/<int:limit>', methods=["GET"])
def get_questions_by_category_limit(category_id, limit):
    """
    Lấy danh sách câu hỏi theo category nhưng bị giới hạn số lượng
    :return:
    """
    try:
        conn = mysql.connect()
        cursor = conn.cursor(pymysql.cursors.DictCursor)
        cursor.execute(constants.QUERY_GET_QUESTION_BY_CATEGORY_LIMIT, (category_id, limit))
        questionsRow = cursor.fetchall()

        respone = jsonify(questionsRow)
        respone.status_code = 200
        return respone
    except Exception as e:
        print(e)
    finally:
        cursor.close()
        conn.close()


# ----------------------------------------------------

@app.errorhandler(404)
def showMessage(error=None):
    """
        Xử lý nếu xảy ra lỗi
        :param error:
        :return:
        """
    message = {
        'status': 404,
        'message': 'Record not found: ' + request.url,
    }
    respone = jsonify(message)
    respone.status_code = 404
    return respone


#
# @app.route("/")
# def show_home_page():
#     # response from the server
#     return "This is home page"


def allowed_file(filename):
    ALLOWED_EXTENSIONS = ['png', 'jpg', 'jpeg', 'gif']
    return '.' in filename and filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS


@app.post("/upload_image")
def upload_image():
    if "image" in request.files:
        filename = request.files["image"].filename
        if allowed_file(filename):
            after_name = photos.save(request.files["image"])
            flash("Image saved successfully.")
            return after_name
        else:
            flash("Image saved unsuccessfully.")
            return "upload unsuccessfully"
    else:
        return "Not key \'image\'"


@app.route('/display/<filename>')
def display_image(filename):
    return redirect(url_for('static', filename='avatar/' + filename), code=301)


if __name__ == "__main__":
    app.run(host="0.0.0.0", debug=True)
