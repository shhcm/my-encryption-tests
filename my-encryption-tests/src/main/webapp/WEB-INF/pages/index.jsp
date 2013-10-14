<html>
<body>
<h2>Hello Encryption Tests!</h2>
<h3>Message: ${subject}</h3>

<form action="/my-encryption-testing/getEncryptionDetails" method="POST">

<select name="mode">
    <option value="ECB"> Electronic Code Book Mode</option>
    <option value="CBC"> Cipher Block Chaining</option>
    <option value="R-CBC"> Randomized Cipher Block Chaining</option>
    <option value="R-CTR"> Randomized Counter</option>
</select>
<input type="submit" value="Submit Details"/>
</form>
</body>
</html>
