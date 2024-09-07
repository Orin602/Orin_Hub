from flask import Flask, request, jsonify
from flask_cors import CORS

app = Flask(__name__)
CORS(app)

# 사용자별 메모 저장을 위한 딕셔너리
user_notes = {}

@app.route('/get_notes', methods=['GET'])
def get_notes():
    user_id = request.args.get('user_id')
    if user_id in user_notes:
        return jsonify(user_notes[user_id])
    else:
        return jsonify({})

@app.route('/save_note', methods=['POST'])
def save_note():
    data = request.json
    user_id = data.get('user_id')
    date = data.get('date')
    content = data.get('content')

    if not user_id or not date or not content:
        return jsonify({'status': 'error', 'message': 'Missing data'}), 400

    if user_id not in user_notes:
        user_notes[user_id] = {}
    if date not in user_notes[user_id]:
        user_notes[user_id][date] = []

    user_notes[user_id][date].append(content)
    return jsonify({'status': 'success'})

@app.route('/delete_note', methods=['POST'])
def delete_note():
    data = request.json
    user_id = data.get('user_id')
    date = data.get('date')
    content = data.get('content')

    if not user_id or not date or not content:
        return jsonify({'status': 'error', 'message': 'Missing data'}), 400

    if user_id in user_notes and date in user_notes[user_id]:
        try:
            user_notes[user_id][date].remove(content)
            if not user_notes[user_id][date]:
                del user_notes[user_id][date]
            if not user_notes[user_id]:
                del user_notes[user_id]
            return jsonify({'status': 'success'})
        except ValueError:
            return jsonify({'status': 'error', 'message': 'Note not found'}), 404
    else:
        return jsonify({'status': 'error', 'message': 'Note not found'}), 404

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5000)

