import requests
from bs4 import BeautifulSoup
import pandas as pd
import time
from datetime import datetime
import os
import re

def extract_rating_parts(text):
    if pd.isna(text):
        return pd.Series([None, None])
    sale_match = re.search(r'판매지수\s*([0-9,]+)', text)
    score_match = re.search(r'총점\s*([0-9]+)', text)
    sale = sale_match.group(1).replace(',', '') if sale_match else None
    score = score_match.group(1) if score_match else None
    return pd.Series([sale, score])

def clean_author(text):
    if pd.isna(text):
        return None
    authors = []
    for part in text.split('/'):
        part = part.strip()
        if part.endswith('저'):
            name = part[:-1].strip()
            authors.append(f"{name} (저)")
        elif part.endswith('역'):
            name = part[:-1].strip()
            authors.append(f"{name} (역)")
        else:
            authors.append(part)
    return ' / '.join(authors)

def crawl_yes24_books():
    books_data = []

    now = datetime.now()
    year = now.year
    month = now.month

    start_page = 1
    end_page = 42

    headers = {
        'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36'
    }

    base_url = "https://www.yes24.com/product/category/monthweekbestseller?categoryNumber=001&pageNumber={}&pageSize=24&type=month&saleYear={}&saleMonth={}"

    for page in range(start_page, end_page + 1):
        url = base_url.format(page, year, month)
        try:
            response = requests.get(url, headers=headers)
            soup = BeautifulSoup(response.text, 'html.parser')
            book_list = soup.find_all('div', class_='itemUnit')

            for book in book_list:
                try:
                    title = book.find('a', class_='gd_name').get_text(strip=True)
                    author = book.find('span', class_='info_auth').get_text(strip=True)
                    publisher = book.find('span', class_='info_pub').get_text(strip=True)
                    date = book.find('span', class_='info_date').get_text(strip=True)

                    img_tag = book.find('img', class_='lazy')
                    image_url = img_tag.get('data-original') if img_tag and img_tag.get('data-original') else img_tag.get('src')

                    sale_num_tag = book.find('span', class_='saleNum')
                    sale_num = sale_num_tag.get_text(strip=True).replace("판매지수 ", "") if sale_num_tag else None

                    rating_tag = book.find('div', class_='info_rating')
                    rating = rating_tag.get_text(strip=True) if rating_tag else None

                    tag = book.find('span', class_='tag')
                    genre = tag.get_text(strip=True) if tag else None

                    books_data.append({
                        'year': year,
                        'month': month,
                        'title': title,
                        'author': author,
                        'publisher': publisher,
                        'image': image_url,
                        'date': date,
                        'sales': sale_num,
                        'rating': rating,
                        'genre': genre
                    })
                except Exception as e:
                    print(f"책 파싱 중 오류 발생: {e}")
                    continue

            time.sleep(1)

        except Exception as e:
            print(f"페이지 요청 오류 발생: {url} | {e}")
            continue

    df = pd.DataFrame(books_data)

    # rating 분리
    df[['sales_count', 'score']] = df['rating'].apply(extract_rating_parts)
    df.drop(columns=['rating'], inplace=True)

    # author 정리
    df['author'] = df['author'].apply(clean_author)

    return df


def save_combined_data(new_df, filename='yes24_books_deduped.csv'):
    if os.path.exists(filename):
        old_df = pd.read_csv(filename)
        combined_df = pd.concat([old_df, new_df], ignore_index=True)
    else:
        combined_df = new_df

    combined_df.drop_duplicates(subset=['title', 'author', 'image', 'publisher'], keep='first', inplace=True)
    combined_df.to_csv(filename, index=False, encoding='utf-8-sig')
    print(f"파일 저장 완료: {filename}")