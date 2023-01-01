# Quiz Game App
🤘 Backend is runned by Flask API (Python) 🤘

🤘 Frontend is designed Android App (Android Studio) 🤘

## 1. Install enviroment Python
* Create env python by Conda
* Install Anaconda (or Miniconda) follow by instructions
    * [Anaconda Installation](https://docs.anaconda.com/anaconda/install/index.html)
    * [Miniconda Installation](https://docs.conda.io/en/main/miniconda.html)
* Create new env and install libraries to run Backend Sever
```commandline
conda create -n server python=3.7.9
conda activate server
```
```commandline
pip install flask==2.2.2
pip install flask-mysql==1.5.2
pip install flask-cors==3.0.10
pip install Flask-Mail==0.9.1
pip install python-dotenv==0.21.0
```

## 2. Run
### 2.1 Import [Database](/Database) to MySQL Workbench
* Instruction for import DB ([Link](https://help.umbler.com/hc/en-us/articles/202385865-MySQL-Importing-Exporting-a-database#:~:text=To%20import%20a%20file%2C%20open,File%20and%20select%20the%20file.))
* Fill config into [config file](/Server/config.py) (Backend Server)
```commandline
app.config["MYSQL_DATABASE_USER"] = "..."
app.config["MYSQL_DATABASE_PASSWORD"] = "..."
app.config["MYSQL_DATABASE_DB"] = "quiz_game"
app.config["MYSQL_DATABASE_HOST"] = "localhost"
```
### 2.2 Run Backend Server
* Open folder [Server](/Server) by Pycharm or VS Code
* Open Terminal and run commands
```commandline
export PYTHONPATH=./
python3 main.py
```
* When server run, It looks like as. Pay attention and the 2nd line _**Running on ...**_ (It may be different on others) to copy to Android app.

![](Images/run_server.png?raw=true)

### 2.3 Run Android App
* Copy url in 2.2 to [file](Android/app/src/main/java/com/example/quizme/utils/APIUtils.java) 
```js 
API_URL = "http://192.168.1.14:5000/"
```

## Code Contributors

:one: [Nam Nguyen Van](https://github.com/ngnambka00-github) 20182698

:two: [Thi Nguyen Viet]() 20182798

:three: [Hoa Pham Thai]() 20182533

:four: [Chung Tran Van]() 20182388


# References
1. [Retrofit](https://www.jackrutorial.com/2018/06/retrofit-2-crud-android-example.html?m=1&fbclid=IwAR3A-EN8ipFW_OhznLEi2bJb_vM7Vql3cTr-EKrOc1dyw6QPrBXmvu7oIFs). A library for creating request between client and server.

```
_/﹋\_
(҂`_´)
<,︻╦╤─ ҉ – – 🍎
_/﹋\_
```