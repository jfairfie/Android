<?php

require 'dbConnection.php';

ob_clean();

$user = $_POST['user'];

$sql = "SHOW GRANTS FOR " . $user . ";";
$stmt = mysqli_prepare($conn, $sql);
$json = array();

if (mysqli_execute($stmt)) {
	$result = mysqli_stmt_get_result($stmt);
	while ($row = mysqli_fetch_assoc($result)) {
		foreach ($row as $r) {
			$tempArray = array("permission" => $r);
			$json[] = $tempArray;
		}
	}
	echo json_encode($json);
} else {
	echo "Unsuccessful";
}	
