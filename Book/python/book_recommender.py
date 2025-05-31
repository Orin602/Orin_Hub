from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity
import random
import pandas as pd

def load_books(file_path='yes24_books_deduped.csv'):
    return pd.read_csv(file_path)

# 랜덤 추천 함수
def random_recommend(df, n=10):
    return df.sample(n)

# 저자 기반 추천 함수
def author_recommend(df, author_name, n=10):
    filtered = df[df['author'].str.contains(author_name, case=False, na=False)]
    return filtered.sample(n) if len(filtered) >= n else filtered

# (title + genre + author + publisher)
def content_recommend(df, title_keyword, n=10):
    df = df.dropna(subset=['title'])
    df = df.reset_index(drop=True)
    df['combined'] = df[['title', 'genre', 'author', 'publisher']].fillna('').agg(' '.join, axis=1)

    vectorizer = TfidfVectorizer()
    tfidf_matrix = vectorizer.fit_transform(df['combined'])
    cosine_sim = cosine_similarity(tfidf_matrix, tfidf_matrix)

    indices = pd.Series(df.index, index=df['title'])

    if title_keyword in indices:
        idx = indices[title_keyword]
    else:
        matching = df[df['combined'].str.contains(title_keyword, case=False, na=False)]
        if matching.empty:
            print(f"❌ '{title_keyword}'를 포함한 책이 없습니다.")
            return pd.DataFrame()
        # ✅ 랜덤하게 기준 책 하나 선택
        idx = random.choice(matching.index.tolist())

    sim_scores = list(enumerate(cosine_sim[idx]))
    sim_scores = sorted(sim_scores, key=lambda x: x[1], reverse=True)[1:n+1]
    book_indices = [i[0] for i in sim_scores]

    return df.loc[book_indices].drop(columns=['combined'])

# 4. 출판사 기반 추천 함수
def publisher_recommend(df, publisher_name, n=5):
    filtered = df[df['publisher'].str.contains(publisher_name, case=False, na=False)]
    return filtered.sample(n) if len(filtered) >= n else filtered

# 5. 해시태그 기반 추천 함수
def hashtag_recommend(df, tag, n=10):
    # null 방지
    df = df.copy()
    df['genre'] = df['genre'].fillna('')

    tag_clean = tag.replace(' ', '').lower()
    filtered = df[df['genre'].apply(lambda tags: tag_clean in tags)]
    return filtered.sample(n) if len(filtered) >= n else filtered
