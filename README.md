# Ad-Auction-Dashboard

## Setting up database
1. Install a MySQL database version `8.0.19 (MySQL Community Server - GPL)`
2. Configure to `localhost` on port `3309`
3. Database set to `sys` if not already
4. To configure with relevant tables run the query below:
    ```
    CREATE TABLE Click (
        Identifier INT,
        Id 	INT                 NOT NULL,
        Date DATETIME           NOT NULL,
        ClickCost FLOAT(8)      NOT NULL,
        PRIMARY KEY (Identifier)
    )
    ```
5. Create a new user with username `application` and password `app_password1`

N/B - Planning to set up a remote database for everyone's convenience soon