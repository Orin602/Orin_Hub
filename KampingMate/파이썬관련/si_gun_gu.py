from flask import Flask, jsonify
import xml.etree.ElementTree as ET

app = Flask(__name__)

@app.route('/get_sigungu', methods=['GET'])
def get_sigungu():
    tree = ET.parse('./sigungu_data.xml')
    root = tree.getroot()
    
    data = []
    for row in root.findall('row'):
        entry = {child.tag: child.text for child in row}
        data.append(entry)
    
    return jsonify(data)

if __name__ == '__main__':
    app.run(debug=True)
