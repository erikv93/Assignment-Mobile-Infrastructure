<?php
// array for JSON response
$response = array();

// check for required fields
if (!empty($_POST['name']) && isset($_POST['score'])) {

	// include db connect class
	require_once __DIR__ . '/db_connect.php';

	// connecting to db
	$db = new DB_CONNECT();

	$name	= $_POST['name'];
	$score	= $_POST['score'];

	$sqlColumn = "name, score";
	$sqlValues = "'".$name."', '".$score."'";

	// mysql inserting a new row
	$result = mysql_query("INSERT INTO Hiscore (".$sqlColumn.") VALUES (".$sqlValues.")");

	// check if row inserted or not
	if ($result) {
		// successfully inserted into database
		$response['success'] = true;
	} else {
		// failed to insert into database
		$response['success'] = false;
		$response['message'] =  mysql_error();
	}
} else {
	// required field is missing
	$response["success"] = false;
	$response["message"] = "Required field(s) is missing";
} 
// echoing JSON response
echo json_encode($response);
?>
