from flask import Blueprint

home_api = Blueprint("home_api", __name__)


@home_api.route("/")
@home_api.route("/home")
def show_home_page():
    # response from the server
    return "This is home page"
