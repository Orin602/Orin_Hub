# pip install oracledb
# pip install schedule
# pip install time


import oracledb
import pandas as pd
import schedule
import time

def fetch_data_and_save():
    # OracleDB에 연결
    connection = oracledb.connect(user='food', password='food', dsn='localhost/XE')

    # SQL 쿼리 실행하여 데이터프레임으로 변환
    query = 'SELECT * FROM food_recipe'
    df = pd.read_sql(query, connection)

    # 데이터프레임을 pickle 파일로 저장
    df.to_pickle('food.pkl')

    # 연결 종료
    connection.close()

# 처음에 한 번 데이터를 가져오고 저장
fetch_data_and_save()

# 매 1분마다 데이터를 업데이트
schedule.every(1).minutes.do(fetch_data_and_save)

# 스케줄러 실행
while True:
    schedule.run_pending()
    time.sleep(1)
