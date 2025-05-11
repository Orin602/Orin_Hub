from flask import Flask, jsonify, request
from book_recommender import load_books, content_recommend, author_recommend, publisher_recommend, random_recommend, hashtag_recommend
from yes24_add_book_data import crawl_yes24_books, save_combined_data
import os
from datetime import datetime, timedelta
from flask_cors import CORS

app = Flask(__name__)
CORS(app)

# 도서 데이터 로드
df = load_books()

def filter_nan_values(data):
    return data.applymap(lambda x: None if isinstance(x, float) and (x != x) else x)

# 랜덤 추천 API
@app.route('/random_recommend')
def random_recommendation():
    df = load_books()
    recommendations = random_recommend(df)
    recommendations = filter_nan_values(recommendations)
    return jsonify(recommendations.to_dict(orient='records'))

# 저자 기반 추천 API
@app.route('/author_recommend')
def author_recommendation():
    df = load_books()
    author_name = request.args.get('author', '')
    recommendations = author_recommend(df, author_name)
    recommendations = filter_nan_values(recommendations)
    return jsonify(recommendations.to_dict(orient='records'))

# 콘텐츠 기반 추천 API
@app.route('/content_recommend')
def content_recommendation():
    df = load_books()
    title_keyword = request.args.get('title', '')
    recommendations = content_recommend(df, title_keyword)
    recommendations = filter_nan_values(recommendations)
    return jsonify(recommendations.to_dict(orient='records'))

# 출판사 기반 추천 API
@app.route('/publisher_recommend')
def publisher_recommendation():
    df = load_books()
    publisher_name = request.args.get('publisher', '')
    recommendations = publisher_recommend(df, publisher_name)
    recommendations = filter_nan_values(recommendations)
    return jsonify(recommendations.to_dict(orient='records'))

# 해시태그 기반 추천 API
@app.route('/hashtag_recommend')
def hashtag_recommendation():
    df = load_books()
    tag = request.args.get('tag', '')
    recommendations = hashtag_recommend(df, tag)
    recommendations = filter_nan_values(recommendations)
    return jsonify(recommendations.to_dict(orient='records'))

# yes24 데이터 업데이트 관리자용 API
@app.route('/update_yes24_books')
def update_yes24_books():
    log_file = 'yes24_last_run.txt'
    history_file = 'yes24_update_history.txt'
    now = datetime.now()

    if os.path.exists(log_file):
        with open(log_file, 'r') as f:
            last_run_str = f.read().strip()
            try:
                last_run = datetime.strptime(last_run_str, "%Y-%m-%d")
                if now - last_run < timedelta(days=365):
                    days_remaining = (timedelta(days=365) - (now - last_run)).days
                    return jsonify({'message': f'이 달의 추천 데이터는 이미 업데이트되었습니다. 다음 업데이트까지 {days_remaining}일 남았습니다.'}), 403
            except:
                pass # 날짜 형식이 잘못되었으면 무시하고 계속 실행

    try:
        df = crawl_yes24_books()
        save_combined_data(df)
        with open(log_file, 'w') as f:
            f.write(now.strftime("%Y-%m-%d"))
        
        # 업데이트 이력 기록
        with open(history_file, 'a') as f:
            f.write(now.strftime("%Y-%m-%d") + '\n')

        return jsonify({'message': 'YES24 도서 데이터가 성공적으로 업데이트되었습니다.'}), 200
    except Exception as e:
        return jsonify({'message': f'업데이트 중 오류 발생: {str(e)}'}), 500


# 최근 업데이트 날짜 표시
@app.route('/last_update_date')
def last_update_date():
    log_file = 'yes24_last_run.txt'
    if os.path.exists(log_file):
        with open(log_file, 'r') as f:
            last_run_str = f.read().strip()
        return jsonify({'last_updated': last_run_str}), 200
    else:
        return jsonify({'last_updated': None}), 200

# 업데이트 날짜 그래프
@app.route('/update_history')
def update_history():
    history_file = 'yes24_update_history.txt'
    if os.path.exists(history_file):
        with open(history_file, 'r') as f:
            dates = [line.strip() for line in f if line.strip()]
        return jsonify([{'date': d} for d in dates])
    else:
        return jsonify([])
    

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5000)
