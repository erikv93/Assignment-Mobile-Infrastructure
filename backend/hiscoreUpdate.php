<?php
// array for JSON response
$response = array();

// check for required fields
if (!empty($_POST['id']) && !empty($_POST['name']) && !empty($_POST['score'])) {

	// include db connect class
	require_once __DIR__ . '/db_connect.php';

	// connecting to db
	$db = new DB_CONNECT();

	// mysql update row with matched name
	$result = mysql_query("UPDATE Hiscore SET name = '".$_POST['name']."', score = '".$_POST['score']."' WHERE id = '".$_POST['id']."'");

	// check if row inserted or not
	if ($result) {
		// successfully updated
		$response["success"] = true;

	} else {
		// unsuccessfully updated
		$response["success"] = false;
		$response["message"] = mysql_error;
	}
} else {
 	// required field is missing
	$response["success"] = false;
	$response["message"] = "Required field(s) is missing";
}
// echoing JSON response
echo json_encode($response);

?>
