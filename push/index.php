<?php
include_once 'class.push.php';
$push = new pushmessage();
$params = array("pushtype"=>"android", "msg"=>"Hello, an android user","registration_id"=>"APA91bGg3RXvYUQex-RlMMbnz7hDSeed4NlFn0TUogbI2wv3cFi3Su6eIjzOfYl3EJk9g3RSK6Y8bwifmJHrA-rq7fEMuslP4lxbiUEEjjb8lMW0ljNGs1B2qev41QddSt_wVkq701pPm0q8Xpcw7UvGUZgYzSJj27RjMMbrydDFBRUoeR3drFw");
$rtn = $push->sendMessage($params);
//print_r($rtn); 

