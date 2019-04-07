<HTML>
<HEAD>
    <TITLE> Creating Account Results </TITLE>
</HEAD>

<BODY>

<H3>
    <?php

    require ('/home/student/tranm346/public_html/.credentials.php');

    // Pull out parameters from the form into local vbles
    $accountNumber = $_POST['accountNumber'];

    if(is_numeric($accountNumber)) {
        // Build the DB query
        $query = "SELECT * FROM tranm346_BANKACCOUNTS where accNum = '" .
            $accountNumber . "'";
        $mysqli = new mysqli();

        // Connect to database
        $mysqli->connect("localhost", $MYSQL_USER, $MYSQL_PW, "tranm346");
        if ($mysqli->errno) {
            printf("Error connecting to database: %s <br />", $mysqli->error);
            exit();
        }

        // Query the database to see if this account exists

        $result = $mysqli->query($query, MYSQLI_STORE_RESULT);
        if ($mysqli->errno) {
            printf("Error in query: %s <br />", $mysqli->error);
            exit();
        }

        // Extract account number from result returned
        $row = $result->fetch_row();
        $queryAccountNum = $row[0];

        if ($queryAccountNum == "") {
            $query = "INSERT INTO tranm346_BANKACCOUNTS VALUES ('" .
                $accountNumber . "', 0)";
            $result = $mysqli->query($query, MYSQLI_STORE_RESULT);
            if ($mysqli->errno) {
                printf("Error in query: %s <br />", $mysqli->error);
                exit();
            }
            echo "Created account '" . $accountNumber . "' successfully";

        } else {
            echo "Account with number '" . $accountNumber . "' already exists";
        }

        // Close the database connection
        $mysqli->close();

    } else {
        echo "Account number '" . $accountNumber . "' is not a valid number";
    }

    ?>

</H3>
<form>
    <button formaction="bank.html">Back to commands</button>
</form>
</BODY>
</HTML>

