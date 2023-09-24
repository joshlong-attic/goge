from flask import Flask, jsonify

# graphql
app = Flask(__name__)

@app.route('/')
def hello_world():
    return jsonify({
        "message": "hello, world from python!"
    })

if __name__ == '__main__':
    app.run(debug=True, port=3000)
