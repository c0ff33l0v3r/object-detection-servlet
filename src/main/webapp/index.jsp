<html>
  <body>
    <head>
      <link rel="stylesheet" href="style.css" />
    </head>
    <div class="header">
      <h1>License Plate Detection</h1>
    </div>

    <form action="upload" enctype="multipart/form-data" method="POST">
      <div class="form-container">
        <input id="filename" type="text" disabled="disabled" placeholder="Filename" />
        <div class="button-container">
          <div class="upload-button">
            <label for="upload-photo">Select Image</label>
            <input
              hidden="hidden"
              type="file"
              name="image-file"
              id="upload-photo"
            />
          </div>
          <button class="submit-button" type="submit">Submit</button>
        </div>
      </div>
    </form>
    <div class="image-container">
      <div style="background: royalblue">
        <img style="max-width: 100%; max-height:
        100%;"src="data:image/jpeg;base64,<%= request .getAttribute("original")
        %>"/>
      </div>
      <div style="background: rebeccapurple">
        <img style="max-width: 100%; max-height:
        100%;"src="data:image/jpeg;base64,<%= request .getAttribute("processed")
        %>"/>
      </div>
	</div>
	<div class="ocr-results-container">
		<div style="background: rebeccapurple; ">
			<img style="max-width: 100%; max-height:
			100%;"src="data:image/jpeg;base64,<%= request.getAttribute("extracted")
			%>"/>
		</div>
		<table>
			<tr>
				<th>Detected License Plate Characters</th>
				<td><%= request.getAttribute("ocrString")%></td>
			</tr>
			<tr>
				<th>License Plate Detection Accuracy</th>
				<td><%= request.getAttribute("lpAccuracy")%></td>
			</tr>
			<tr>
				<th>OCR Accuracy</th>
				<td><%= request.getAttribute("ocrAccuracy")%></td>
			</tr>
		</table>
	</div>
  </body>
</html>
