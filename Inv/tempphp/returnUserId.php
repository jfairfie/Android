<?php

require 'dbConnection.php';

$username = $_POST['username'];
$sql = "SELECT userid FROM User WHERE username=?;";
$stmt = mysqli_prepare($conn, $sql);
mysqli_stmt_bind_param($stmt, "s", $username);
mysqli_stmt_execute($stmt);
$result = mysqli_stmt_get_result($stmt);
$row = mysqli_fetch_assoc($result);

$userid = $row['userid'];