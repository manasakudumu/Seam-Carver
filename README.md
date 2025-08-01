# Seam Carver Image Resizing

This project implements content-aware image resizing using the Seam Carving algorithm. It allows dynamic removal of vertical or horizontal seams with minimal visual distortion, preserving important image features.

It's powered by a Java-based backend, using dynamic programming for seam pathfinding and pixel energy computation. The user interface is built using a Flask-based Python frontend.

## Features

-  **Content-aware resizing**: Reduces image width or height while preserving important features like objects and edges.
-  **Dual-gradient energy function**: Computes pixel importance based on color gradients.
-  **Dynamic Programming**: Efficiently finds seams of minimum energy.
-  **Seam removal**: Removes vertical and horizontal seams using greedy pathfinding.
-  **Web interface**: Upload and visualize carved results via a Flask web server.
-  **Compare View**: Side-by-side comparison of the original and resized images.

## Technologies Used

- **Java** 
- **Python (Flask)** 
- **Pillow** 
- **HTML/CSS** 




