-- SQL Seed Script for Loan Data (T-SQL / SQL Server)
-- Generated on 2026-01-15

-- Variables for seeding
DECLARE @i INT = 1;
DECLARE @userId UNIQUEIDENTIFIER;
DECLARE @roleId UNIQUEIDENTIFIER;
DECLARE @productId UNIQUEIDENTIFIER;
DECLARE @loanId UNIQUEIDENTIFIER;
DECLARE @customerDetailId UNIQUEIDENTIFIER;
DECLARE @adminId UNIQUEIDENTIFIER;

-- Specific formula variables
DECLARE @tenor INT;
DECLARE @interestRate FLOAT;
DECLARE @principalDebt BIGINT;
DECLARE @interestAmount BIGINT;
DECLARE @outstandingDebt BIGINT;
DECLARE @installmentPayment BIGINT;

-- Get "CUSTOMER" role ID
SELECT TOP 1 @roleId = id FROM roles WHERE name = 'CUSTOMER';

-- Get "ADMIN_USER" ID for createdBy/updatedBy
SELECT TOP 1 @adminId = id FROM users WHERE username = 'ADMIN_USER';

-- Create 20 new users
WHILE @i <= 20
BEGIN
    SET @userId = NEWID();
    
    -- Insert User (Password: Password123!)
    INSERT INTO users (id, username, email, password, is_active, is_deleted, created_at, created_by)
    VALUES (
        @userId, 
        'testuser' + CAST(@i AS VARCHAR(2)), 
        'testuser' + CAST(@i AS VARCHAR(2)) + '@example.com', 
        '$2a$10$8.UnVuG9HHgffUdAlk8qfOuVGkqRzgVymGe07xd00DMxs.7uKCQqO', -- BCrypt for 'Password123!'
        1, 
        0, 
        DATEADD(MONTH, -(@i % 24), GETDATE()), 
        @adminId
    );

    -- Assign CUSTOMER role
    INSERT INTO user_roles (user_id, role_id)
    VALUES (@userId, @roleId);

    -- Insert Customer Detail
    SET @customerDetailId = NEWID();
    INSERT INTO customer_details (
        id, user_id, full_name, national_id, citizenship, place_of_birth, 
        date_of_birth, is_male, religion, marital_status, phone_number, 
        address, zip_code, house_status, job, workplace, salary, account_number, is_deleted, created_at, created_by
    )
    VALUES (
        @customerDetailId,
        @userId,
        'Full Name Test ' + CAST(@i AS VARCHAR(2)),
        '320101' + CAST(100000 + @i AS VARCHAR(10)),
        'WNI',
        'Jakarta',
        '1990-01-01',
        1,
        'ISLAM',
        'SINGLE',
        '0812345678' + CAST(@i AS VARCHAR(2)),
        'Jl. Raya No. ' + CAST(@i AS VARCHAR(2)),
        '12345',
        'OWNED',
        'Employee',
        'Company ' + CAST(@i AS VARCHAR(2)),
        5000000.0 + (@i * 100000),
        '123456789' + CAST(@i AS VARCHAR(2)),
        0,
        GETDATE(),
        @adminId
    );

    -- Create 5 loans for each user
    DECLARE @j INT = 1;
    WHILE @j <= 5
    BEGIN
        SET @loanId = NEWID();
        
        -- Pick a random product and get its tenor/interestRate
        SELECT TOP 1 
            @productId = id, 
            @tenor = tenor, 
            @interestRate = interest_rate -- Note: Use interest_rate as per snake_case if table uses it, or entity mapping
        FROM products ORDER BY NEWID();

        -- Pick a random status
        DECLARE @status VARCHAR(20);
        DECLARE @randStatus INT = ABS(CHECKSUM(NEWID())) % 5;
        SET @status = CASE @randStatus 
            WHEN 0 THEN 'CREATED'
            WHEN 1 THEN 'REVIEWED'
            WHEN 2 THEN 'APPROVED'
            WHEN 3 THEN 'REJECTED'
            WHEN 4 THEN 'DISBURSED'
        END;

        -- Randomly subtract months from current date (Jan 2026)
        DECLARE @createdDate DATETIME = DATEADD(MONTH, -((ABS(CHECKSUM(NEWID())) % 24) + 1), GETDATE());

        -- Formula Logic from LoanService.java:
        -- interestAmount = ROUND(principalDebt * (interestRate / 100) * tenor)
        -- outstandingDebt = principalDebt + interestAmount
        -- installmentPayment = outstandingDebt / tenor

        SET @principalDebt = 5000000 * @j;
        SET @interestAmount = ROUND(@principalDebt * (@interestRate / 100.0) * @tenor, 0);
        SET @outstandingDebt = @principalDebt + @interestAmount;
        SET @installmentPayment = @outstandingDebt / @tenor;

        -- Insert Loan
        INSERT INTO loans (
            id, product_id, principal_debt, outstanding_debt, tenor, 
            interest_rate, installment_payment, status, is_deleted, created_at, created_by
        )
        VALUES (
            @loanId,
            @productId,
            @principalDebt,
            @outstandingDebt,
            @tenor,
            @interestRate,
            @installmentPayment,
            @status,
            0,
            @createdDate,
            @userId
        );

        -- Insert Loan Status History (Initial CREATED)
        INSERT INTO loan_status_histories (id, loan_id, action, note, performed_by, performed_at)
        VALUES (
            NEWID(),
            @loanId,
            'CREATED',
            'Initial loan application',
            @userId,
            @createdDate
        );

        -- Insert extra history based on status
        IF @status IN ('REVIEWED', 'APPROVED', 'REJECTED', 'DISBURSED')
        BEGIN
            INSERT INTO loan_status_histories (id, loan_id, action, note, performed_by, performed_at)
            VALUES (NEWID(), @loanId, 'REVIEWED', 'Reviewing documents', @adminId, DATEADD(DAY, 1, @createdDate));
        END

        IF @status IN ('APPROVED', 'REJECTED', 'DISBURSED')
        BEGIN
            DECLARE @finalAction VARCHAR(20) = CASE WHEN @status = 'REJECTED' THEN 'REJECTED' ELSE 'APPROVED' END;
            INSERT INTO loan_status_histories (id, loan_id, action, note, performed_by, performed_at)
            VALUES (NEWID(), @loanId, @finalAction, 'Final decision', @adminId, DATEADD(DAY, 2, @createdDate));
        END

        IF @status = 'DISBURSED'
        BEGIN
            INSERT INTO loan_status_histories (id, loan_id, action, note, performed_by, performed_at)
            VALUES (NEWID(), @loanId, 'DISBURSED', 'Funds transferred', @adminId, DATEADD(DAY, 3, @createdDate));
        END

        SET @j = @j + 1;
    END

    SET @i = @i + 1;
END
GO
