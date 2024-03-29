# ぬめろん セットアップマニュアル

## 目次

1. [サーバへのアクセス](#サーバへのアクセス)
    - 1.1 [サーバIPとパスワード](#1-サーバipとパスワード)
    - 1.2 [サーバへのアクセス手順](#2-サーバへのアクセス手順)
    - 1.3 [パスワードの入力](#3-パスワードの入力)

2. [Webアプリケーションの公開](#webアプリケーションの公開)
    - 2.1 [Gradleを利用してアプリケーションを公開](#1-gradleを利用してアプリケーションを公開)
    - 2.2 [ホームディレクトリへの移動](#2-ホームディレクトリへの移動)
    - 2.3 [GitHubからリポジトリを取得](#3-githubからリポジトリを取得)
    - 2.4 [Gradleを利用したWebアプリケーションの実行](#4-gradleを利用したwebアプリケーションの実行)
    - 2.5 [Webアプリケーションのアクセス](#5-webアプリケーションのアクセス)

---

## サーバへのアクセス

### 1. サーバIPとパスワード

- **IPアドレス:** `150.89.233.202`
- **パスワード:** `isDev23?202`

   > 注意: サーバは大学内のネットワーク上にあります。接続する際はVPNを利用して接続してください。

### 2. サーバへのアクセス手順

   - `isdev-bash-....exe`を起動し、以下のSSHコマンドでサーバにアクセスします。

     ```bash
     $ ssh isdev23@150.89.233.202
     ```

### 3. パスワードの入力

   - パスワードを求められるので、以下のように入力します。

     ```bash
     isdev23@150.89.233.202's password: isDev23?202
     ```

     > 注意: パスワードは画面に表示されません。

   - アクセス成功時、以下のようなメッセージが表示されます。

     ```bash
      user_name@host_name MINGW64 ~
      $ ssh isdev23@150.89.233.202
      Warning: Permanently added '150.89.233.202' (ED25519) to the list of known hosts.
      Welcome to Ubuntu 22.04.3 LTS (GNU/Linux 6.2.0-35-generic x86_64)

      * Documentation:  https://help.ubuntu.com
      * Management:     https://landscape.canonical.com
      * Support:        https://ubuntu.com/advantage

        System information as of Fri Oct 27 02:19:11 UTC 2023

        System load:    0.70556640625   Temperature:           53.0 C
        Usage of /home: unknown         Processes:             27
        Memory usage:   0%              Users logged in:       0
        Swap usage:     0%              IPv4 address for eth0: 150.89.233.XXX

      * Strictly confined Kubernetes makes edge and IoT secure. Learn how MicroK8s
        just raised the bar for easy, resilient and secure K8s cluster deployment.

        https://ubuntu.com/engage/secure-kubernetes-at-the-edge

      Expanded Security Maintenance for Applications is not enabled.

      0 updates can be applied immediately.

      Enable ESM Apps to receive additional future security updates.
      See https://ubuntu.com/esm or run: sudo pro status

      Last login: Fri Oct 27 00:52:53 2023 from XXX.XXX.XXX.XXX
     ```

## Webアプリケーションの公開

### 1. Gradleを利用してアプリケーションを公開

### 2. ホームディレクトリへの移動

   - ホームディレクトリに移動します。

     ```bash
     $ cd
     ```

   - 移動後、ホームディレクトリが正しく設定されているかを確認します。

     ```bash
     $ pwd
     ```

     以下のように表示されていることを確認します。

     ```
     isdev23@ubuntu202:~$ pwd
     /home/isdev23
     ```

### 3. GitHubからリポジトリを取得

   - リポジトリをクローンします。

     ```bash
     $ git clone https://github.com/e1b21069/team2.git
     ```

   - リポジトリが正しくクローンされたかを確認するため、`ls` コマンドで確認します。

     ```bash
     $ ls
     ```

     以下のような結果が表示されていることを確認します。

     ```
     isdev23@ubuntuXXX:~$ ls
     team2
     ```

   - もしサーバに元々アプリケーションのディレクトリがある場合は、そのディレクトリに移動し以下のコマンドを実行します。

     ```bash
     $ git pull origin main
     ```

### 4. Gradleを利用したWebアプリケーションの実行

   - プロジェクトのディレクトリに移動します。

     ```bash
     $ cd team2
     ```

   - `gradlew`をbashを利用して実行します。

     ```bash
     $ bash ./gradlew
     ```

   - アプリケーションを実行するためのコマンドを実行します。

     ```bash
     $ bash ./gradlew bootrun
     ```

### 5. Webアプリケーションのアクセス

   - アプリケーションが公開されるため、以下のURLにアクセスできます。

     - URL: [http://150.89.233.202](http://150.89.233.202)

---

以上、セットアップマニュアルをご確認いただきありがとうございます。