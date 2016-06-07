<?php
// array for JSON response
$response = array();

// include db connect class
require_once __DIR__ . '/db_connect.php';

// connecting to db
$db = new DB_CONNECT();

$sqlQuery = "SELECT * FROM Hiscore WHERE ";
$sqlCopy = $sqlQuery;

if (!empty($_POST['id'])){
	$sqlQuery .= "id = '".$_POST['id']."'";
} else if (!empty($_GET['id'])){
	$sqlQuery .= "id = '".$_GET['id']."'";
}

if ($sqlQuery != $sqlCopy) $sqlQuery .= " AND ";
$sqlCopy = $sqlQuery;

if (!empty($_POST['name'])){
	$sqlQuery .= "name LIKE '".$_POST['name']."'";
} else if (!empty($_GET['name'])){
	$sqlQuery .= "name LIKE '".$_GET['name']."'";
}

if ($sqlQuery != $sqlCopy) $sqlQuery .= " AND ";
$sqlCopy = $sqlQuery;

if (!empty($_POST['score'])){
	$sqlQuery .= "score LIKE '".$_POST['score']."'";
} else if (!empty($_GET['score'])){
	$sqlQuery .= "score LIKE '".$_GET['score']."'";
}

if (substr($sqlQuery, -4) == "AND ") $sqlQuery = substr_replace($sqlQuery,"",-5);
if (substr($sqlQuery, -6) == "WHERE ") $sqlQuery = substr_replace($sqlQuery,"",-7);

$sqlQuery .= " ORDER BY score DESC";
//$response['query'] = $sqlQuery;

$sqlResult = mysql_query($sqlQuery);
	
if (!empty($sqlResult)) {
	// check for empty result
	if (mysql_num_rows($sqlResult) > 0) {
		$response['success'] = true;
		$int = 0;
		while ($row = mysql_fetch_assoc($sqlResult)) {
			$hiscore['id']		= $row['id'];
			$hiscore['name']	= $row['name'];
			$hiscore['score']	= $row['score'];

			$response['hiscore'][$int] = $hiscore;
			$int++;
		}
	} else {
		// no hiscore found
		$response["success"] = false;
		$response["message"] = "No hiscore found";
		}
} else {
	$response["success"] = false;
	$response["message"] = mysql_error();
}
// echo JSON response
echo json_encode($response);
?>

