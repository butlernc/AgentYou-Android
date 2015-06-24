<?php
// These code snippets use an open-source library. http://unirest.io/php
$response = Unirest\Request::post("https://lambda-face-recognition.p.mashape.com/album_train",
  array(
    "X-Mashape-Key" => "PLOBKE1Og8mshUBLVIHnXB2qa2f3p1Gdl61jsnT8btjdllfdBz",
    "Content-Type" => "application/x-www-form-urlencoded",
    "Accept" => "application/json"
  ),
  array(
    "album" => $album,
    "albumkey" => $albumkey,
    "entryid" => "email",
    "files" => dirname(__FILE__) . "../image/" . $image
  )
);

?>


http://hmkcode.com/android-send-json-data-to-server/