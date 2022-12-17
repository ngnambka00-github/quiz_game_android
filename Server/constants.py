QUERY_INSERT_USER = "INSERT INTO user(fullname, email, password, coin) VALUES(%s, %s, %s, '250')"
QUERY_GET_USERS = "SELECT * FROM user"
QUERY_GET_USER_BY_ID = "SELECT * FROM user WHERE user_id=%s"
QUERY_UPDATE_USER = "UPDATE user SET email=%s, password=%s, coin=%s, fullname=%s WHERE user_id=%s"
QUERY_DELETE_USER = "DELETE FROM user WHERE user_id =%s"

QUERY_INSERT_CATEGORY = "INSERT INTO category(category_name, category_image) VALUES(%s, %s)"
QUERY_GET_CATEGORIES = "SELECT * FROM category"

QUERY_INSERT_QUESTION = "INSERT INTO question(question, option1, option2, option3, option4, answer, category_id) VALUES(%s, %s, %s, %s, %s, %s, %s)"
QUERY_GET_QUESTION_BY_CATEGORY = "SELECT * FROM question WHERE category_id=%s"
QUERY_GET_QUESTION_BY_CATEGORY_LIMIT = "SELECT * FROM question WHERE category_id=%s ORDER BY RAND() LIMIT %s"
