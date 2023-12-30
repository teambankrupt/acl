package com.example.acl.domains.users.models.constants

fun otpTemplate(brand: String, otp: String)= """
    
    <!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${brand} - OTP Verification</title>
    <style>
        body {
            font-family: sans-serif;
            margin: 0;
            padding: 20px;
            text-align: center;
        }
        .container {
            width: 80%;
            max-width: 600px;
            margin: 0 auto;
        }
        h1 {
            font-size: 24px;
            margin-bottom: 15px;
        }
        .otp-code {
            font-size: 36px;
            font-weight: bold;
            background-color: #f0f0f0;
            padding: 10px 20px;
            border-radius: 5px;
            margin-bottom: 20px;
        }
        p {
            font-size: 16px;
            line-height: 1.5;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>$brand</h1>
        <p>Your one-time password (OTP) is:</p>
        <div class="otp-code">$otp</div>
        <p>Please enter this code to verify your identity.</p>
        <p>If you didn't request this code, please ignore this email.</p>
        <p>Thanks,<br>$brand</p>
    </div>
</body>
</html>
    
"""