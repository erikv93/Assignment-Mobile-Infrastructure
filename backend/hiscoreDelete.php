<?php
// array for JSON response
$response = array();

// check for required fields
if (!empty($_POST['id'])) {
	// include db connect class
	require_once __DIR__ . '/db_connect.php';

	// connecting to db
	$db = new DB_CONNECT();

	// mysql delete row with matched name
	$result = mysql_query("DELETE FROM Hiscore WHERE id = '".$_POST['id']."'");

	// check if row deleted or not
	if ($result) {
		// successfully deleted
		$response["success"] = true;
	} else {
		// unsuccessfully deleted
		$response["success"] = false;
		$response["message"] = "Hiscore not found.";
	}
} else {
	// required field is missing
	$response["success"] = false;
	$response["message"] = "Required field(s) is missing";
}
// echoing JSON response
echo json_encode($response);
?>
