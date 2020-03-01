# Ad-Auction-Dashboard

## Setting up database
1. Install a MySQL database version `8.0.19 (MySQL Community Server - GPL)`
2. Configure to `localhost` on port `3309`
3. Create new schema/database named `app` if not already
4. To configure with relevant tables run the query below:
    ```
    CREATE TABLE app.Click (
        Identifier INT,
        Campaign VARCHAR(255)   NOT NULL,
        Date DATETIME           NOT NULL,
        Id BIGINT               NOT NULL,
        ClickCost FLOAT(8)      NOT NULL,
        PRIMARY KEY (Identifier)
    )
   
    CREATE TABLE app.Impression (
        Identifier INT,
        Campaign VARCHAR(255)   NOT NULL,
        Date DATETIME           NOT NULL,
        Id BIGINT               NOT NULL,
        Gender TINYINT          NOT NULL,
        Age TINYINT             NOT NULL,
        Income TINYINT          NOT NULL,
        Context TINYINT         NOT NULL,
        ImpressionCost FLOAT(8) NOT NULL,
        PRIMARY KEY (Identifier)
   )
    ```
5. Create a new user with username `application` and password `app_password1`

N/B - Planning to set up a remote database for everyone's convenience soon