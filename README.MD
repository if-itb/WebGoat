# Laporan Tugas IF5192 Static Code Analysis dan Bug Fixing pada WebGoat
***Oleh : Fawwaz Muhammad***
***NIM : 23515066***

### A. Pencarian Bug menggunakan FindBugs
Pada tugas ini saya menggunakan Findbugs yang terintegrasi dengan NetBeans. Setelah melakukan pemeriksaan, didapatkan banyak bug. Namun karena concern matakuliah ini hanya pada aspek keamanan, kategori yang diperiksa dalam tugas ini hanya kategori Security dan Malicious Code Vulnerability.
![Screenshot hasil findbugs](https://raw.githubusercontent.com/fawwaz/WebGoat/master/laporan/findbugs.JPG)
### B. Bug Fixing
#### B.1 Security Vulnerability
##### B.1.1 Empty Database Password (UserDatabase.java:51)
Webgoat menggunakan H2 sebagai DBMS. H2 adalah sistem database yang menyimpan data di dalam memori. Mengacu pada [dokumentasi H2](http://www.h2database.com/html/tutorial.html), H2 secara otomatis membuat database baru jika databasenya belum pernah ada sebelumnya. User yang pertamakali menciptakan database akan dianggap sebagai administrator. Sayangnya webgoat membuat koneksi ke H2 seperti yang dicontohkan oleh H2 (dengan string kosong), hal ini akan sangat rawan karena H2 bekerja di memory. Seandainya ada program jahat yang mencoba mengeksploitasi memori dan menggunakan default password seperti yang dicontohkan oleh H2 dalam tutorialnya, informasi ini akan bocor. Oleh karena itu dalam proses bug fixing digunakan password dari file external.
```
public boolean open() {
        BufferedReader br = null;
        StringBuffer sb = new StringBuffer("");
        try {

            String sCurrentLine;

            br = new BufferedReader(new FileReader("password.txt"));

            while ((sCurrentLine = br.readLine()) != null) {
                sb.append(sCurrentLine);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
        try {
            if (userDB == null || userDB.isClosed()) {
                Class.forName("org.h2.Driver");
                userDB = DriverManager.getConnection(USER_DB_URI, "webgoat_admin", sb.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
```
##### B.1.2 Empty Database Password (DatabaseUtilities.java:134)
Sama seperti poin sebelumnya.
##### B.1.3 Non Constant String passed to execute method on SQL Statement(AbstractLesson.java:731)
Dalam file AbstractLesson, query yang dibentuk menggunakan konkatenasi string. Hal ini sangat rawan terhadap injection, oleh karena itu diperbaiki dengan prepared statement.
```
public boolean isAuthorized(WebSession s, String role, String functionId) {
        logger.info("Checking if " + role + " authorized for: " + functionId);
        boolean authorized = false;
        try {
            String query = "SELECT * FROM auth WHERE role = ? and functionid = ?";
            try {
                PreparedStatement statement = WebSession.getConnection(s).prepareStatement(query);
                statement.setString(1, role);
                statement.setString(2, functionId);
                ResultSet answer_results = statement.executeQuery();
                authorized = answer_results.first();
                logger.info("authorized: " + authorized);
            } catch (SQLException sqle) {
                s.setMessage("Error authorizing");
                logger.error("Error authorizing", sqle);
            }
        } catch (Exception e) {
            s.setMessage("Error authorizing");
            logger.error("Error authorizing", e);
        }
        return authorized;
    }
```
##### B.1.4 Non Constant String passed to execute method on SQL Statement(ViewDatabase.java:82)
WebGoat adalah project untuk menjelaskan secara langsung vulnerability yang sering ditemui di lapangan. File ViewDatabase.java memang didesain untuk menerima input SQL raw dari Websession milik User. Oleh karena itu tidak perlu ada perubahan pada file ini.
##### B.1.5 Non Constant String passed to execute method on SQL Statement(CreateDB.java:998)
![Konkatenasi aman](https://raw.githubusercontent.com/fawwaz/WebGoat/master/laporan/aman.JPG)
Dalam file CreateDB, digunakan konkatenasi string. Meskipun demikian, string yang digunakan untuk konkatenasi bukan berasal dari input user melainkan di Hardcode pada file(Dapat dilihat pada gambar diatas). Oleh karena itu konkatenasi ini dapat dikatakan aman.
##### B.1.6 Non Constant String passed to execute method on SQL Statement(UserDatabase.java:90)
#### B.2 Malicious Code Vulnerability
##### B.2.1 Field Should Be Package Protected (HammerHead.java:78)
##### B.2.2 Field isn't final but should be (Screen.java:53)
##### B.2.3 Field isn't final but can't be protected from malicious Code(Course.java:70)
### Kesimpulan
