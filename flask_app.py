from flask import Flask, render_template, request, redirect, url_for, send_from_directory
from PIL import Image
import subprocess
import os
import shutil
import glob

app = Flask(__name__)


UPLOAD_FOLDER = 'input'
OUTPUT_FOLDER = 'output'
STATIC_FOLDER = 'static'

INPUT_FILE = os.path.join(UPLOAD_FOLDER, 'input.png')
OUTPUT_FILE = os.path.join(OUTPUT_FOLDER, 'out.png')
STATIC_INPUT_COPY = os.path.join(STATIC_FOLDER, 'input.png')  # used in compare view

# routes
@app.route('/')
def index():
    return render_template('index.html')

@app.route('/upload', methods=['POST'])
def upload():
    file = request.files.get('image')
    vertical = request.form.get('vertical', '0')
    horizontal = request.form.get('horizontal', '0')
    
    if not file:
        return 'No file uploaded', 400

    try:
        os.makedirs(UPLOAD_FOLDER, exist_ok=True)
        os.makedirs(OUTPUT_FOLDER, exist_ok=True)
        os.makedirs(STATIC_FOLDER, exist_ok=True)

        file.save(INPUT_FILE)
        shutil.copy(INPUT_FILE, STATIC_INPUT_COPY)

        # Compile Java files in src/
        java_files = glob.glob("src/*.java")
        subprocess.run(["javac", "-d", "src"] + java_files, check=True)  # compile with correct output dir
        subprocess.run(["java", "-cp", "src", "src.Main", str(vertical), str(horizontal)], check=True)



        return redirect(url_for('result'))

    except subprocess.CalledProcessError as e:
        return f"Java error: {e}", 500
    except Exception as e:
        return f"Unexpected error: {e}", 500

@app.route('/result')
def result():
    return render_template('result.html')

@app.route('/compare')
def compare():
    try:
        orig = Image.open(STATIC_INPUT_COPY)
        carved = Image.open(OUTPUT_FILE)

        return render_template('compare.html',
            original_width=orig.width,
            original_height=orig.height,
            carved_width=carved.width,
            carved_height=carved.height
        )
    except Exception as e:
        return f"Error loading images: {e}", 500

@app.route('/output/<filename>')
def output_file(filename):
    return send_from_directory(OUTPUT_FOLDER, filename)

@app.route('/static/<path:filename>')
def static_file(filename):
    return send_from_directory(STATIC_FOLDER, filename)

if __name__ == '__main__':
    app.run(debug=True, port=5050)

