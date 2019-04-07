<HTML>
<HEAD>
    <TITLE> Withdraw Results </TITLE>
</HEAD>

<BODY>

<H3>
    <?php

    require ('/home/student/tranm346/public_html/.credentials.php');

    // Pull out parameters from the form into local vbles
    $accountNumber = $_POST['accountNumber'];
    $withdrawAmount = $_POST['withdrawAmount'];

    if(is_numeric($accountNumber) && is_numeric($withdrawAmount)) {

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
            echo "There is no account with number '" . $accountNumber .
                "' in the bank to deposit into";
        } else {
            $currentBalance = $row[1];
            if ($currentBalance - $withdrawAmount >= 0) {
                $newBalance = $currentBalance - $withdrawAmount;

                $query = "UPDATE tranm346_BANKACCOUNTS SET balance = " . $newBalance .
                    " WHERE accNum = " . $accountNumber;
                $result = $mysqli->query($query, MYSQLI_STORE_RESULT);
                if ($mysqli->errno) {
                    printf("Error in query: %s <br />", $mysqli->error);
                    exit();
                }
                echo "Withdrew $" . $withdrawAmount .
                    " successfully. New balance: $" . $newBalance;
            } else {
                echo "There are not enough funds to withdraw $" . $withdrawAmount .
                    ". Balance is: $" . $currentBalance;
            }
        }

        // Close the database connection
        $mysqli->close();
    } else {
        echo "Account number '" . $accountNumber .
            "' or withdraw amount '" . $withdrawAmount . "' is not a valid number";
    }

    ?>

</H3>
<form>
    <button formaction="bank.html">Back to commands</button>
</form>
</BODY>
</HTML>

