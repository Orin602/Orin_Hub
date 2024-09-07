import oracledb
import pandas as pd
import schedule
import time
import os

def fetch_data_and_save():
    try:
        # OracleDB에 연결
        connection = oracledb.connect(user='kamp', password='kamp', dsn='localhost/XE')
        
        # SQL 쿼리 실행하여 데이터프레임으로 변환
        query = 'SELECT * FROM go_camping'
        df = pd.read_sql(query, connection)
        
        # 데이터프레임을 지정된 경로에 pickle 파일로 저장
        file_path = os.path.join('E:/Student/API', 'GoCamping.pkl')
        df.to_pickle(file_path)
        
        print(f'Data saved to {file_path}')
    
    except Exception as e:
        print(f'Error: {e}')
    
    finally:
        # 연결 종료
        if connection:
            connection.close()

def fetch_data_and_save2():
    try:
        # OracleDB에 연결
        connection = oracledb.connect(user='kamp', password='kamp', dsn='localhost/XE')
        
        # SQL 쿼리 실행하여 데이터프레임으로 변환
        query = 'SELECT kakao_id, reviewrate, id FROM review'
        df = pd.read_sql(query, connection)
        
        # 데이터프레임을 지정된 경로에 pickle 파일로 저장
        file_path = os.path.join('E:/Student/API', 'memberRate.pkl')
        df.to_pickle(file_path)
        
        print(f'Data saved to {file_path}')
    
    except Exception as e:
        print(f'Error: {e}')
    
    finally:
        # 연결 종료
        if connection:
            connection.close()

# 처음에 한 번 데이터를 가져오고 저장
fetch_data_and_save()
fetch_data_and_save2()

# 매 1분마다 데이터를 업데이트
schedule.every(1).minutes.do(fetch_data_and_save)
schedule.every(1).minutes.do(fetch_data_and_save2)

# 스케줄러 실행
while True:
    schedule.run_pending()
    time.sleep(1)
