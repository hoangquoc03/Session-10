# Bài tập 1: Khởi tạo và kết nối máy chủ Linux

## Môi trường thực hành

Do không sử dụng tài khoản Cloud, bài này được thực hiện bằng Ubuntu 22.04.5 LTS chạy trong WSL2 trên Windows. OpenSSH Server đã được cài và bật; tài khoản SSH riêng là `devops`.

> Đây là phương án máy cục bộ thay thế theo yêu cầu bài tập, không phải VPS Cloud và không có địa chỉ IP public.

## Kiểm tra máy Linux

Trong PowerShell:

```powershell
wsl --list --verbose
```

Kết quả cần có distro `Ubuntu-22.04` với `VERSION` là `2`.

## Kết nối SSH

WSL tự dừng distro khi không còn tiến trình. Trước tiên, mở một cửa sổ PowerShell riêng và giữ Ubuntu chạy:

```powershell
wsl -d Ubuntu-22.04 -u root
```

Để cửa sổ đó mở. Trong một cửa sổ PowerShell khác, kết nối SSH:

```powershell
ssh -i "$env:TEMP\session10-lab-key" devops@localhost
```

Sau khi đăng nhập, kiểm tra user và hệ điều hành:

```bash
whoami
cat /etc/os-release
```

Kết nối đã được kiểm chứng từ Windows tới Ubuntu 22.04.5 LTS qua `localhost`; user đăng nhập là `devops`.

## Ảnh bằng chứng cần bổ sung

Chụp màn hình thật bằng Snipping Tool (`Win+Shift+S`) và lưu vào thư mục `evidence/` trong repo:

- `01-wsl-ubuntu-configuration.png`: PowerShell hiển thị `wsl --list --verbose`, gồm distro Ubuntu 22.04 và WSL version 2.
- `02-ssh-login.png`: cửa sổ PowerShell sau khi SSH đăng nhập, hiển thị lệnh `whoami` và thông tin Ubuntu 22.04.

Ảnh cấu hình Cloud/IP public không có trong phương án local này; không dùng ảnh giả thay cho bằng chứng VPS Cloud.

## Lưu ý khóa SSH

Khóa lab được tạo tạm trong thư mục Temp của Windows, không nằm trong repo. Không đưa khóa riêng `session10-lab-key` lên GitHub. Sau khi chụp ảnh và hoàn tất bài, có thể xóa khóa bằng PowerShell:

```powershell
Remove-Item "$env:TEMP\session10-lab-key*"
```

## Bài tập 2: User deploy và Docker

Trong Ubuntu 22.04 WSL2, đã tạo user `deploy`, thêm vào hai nhóm `sudo` và `docker`, cài Docker Engine cùng Docker Compose plugin từ repository APT chính thức của Docker. Docker service ở trạng thái `active`; container `hello-world` chạy thành công.

Phiên bản đã kiểm tra dưới user `deploy`:

```text
Docker version 29.8.2, build 7fc2dff
Docker Compose version v5.6.0
```

Danh sách lệnh Linux đã thực thi: [commands-bai-2.txt](commands-bai-2.txt).

User `deploy` đã thuộc nhóm `sudo`, nhưng hiện chưa đặt mật khẩu. Để dùng `sudo` tương tác, mở Ubuntu bằng root và tự đặt mật khẩu cục bộ:

```bash
passwd deploy
```

Không chia sẻ mật khẩu trong chat hoặc đưa mật khẩu vào Git.

### Ảnh terminal cần bổ sung

Mở Ubuntu 22.04 bằng root và giữ cửa sổ đó mở:

```powershell
wsl -d Ubuntu-22.04 -u root
```

Trong Ubuntu, chạy:

```bash
su - deploy
whoami
docker --version
docker compose version
```

Chụp ảnh thật bằng `Win+Shift+S` và lưu thành `evidence/03-deploy-docker-version.png`. Ảnh này là bằng chứng local WSL2, không phải VPS Cloud.

## Bài tập 3: Tăng cường bảo mật SSH

Đã tạo khóa Ed25519 riêng cho `deploy` trên Windows, cài public key vào `/home/deploy/.ssh/authorized_keys` với quyền thư mục `700` và file `600`. Private key nằm trong thư mục Temp của Windows, không có trong repo.

Trong `/etc/ssh/sshd_config`, các cấu hình sau được đặt trước `Include`:

```text
PermitRootLogin no
PasswordAuthentication no
PubkeyAuthentication yes
KbdInteractiveAuthentication no
```

`sshd -t` thành công; `sshd -T` xác nhận root login và password authentication bị tắt, public-key authentication được bật. SSH service đã được restart. Đăng nhập `deploy` bằng key thành công; thử đăng nhập `root` bị từ chối:

```text
root@localhost: Permission denied (publickey).
```

Các lệnh đã thực thi: [commands-bai-3.txt](commands-bai-3.txt).

### Ảnh từ chối root cần bổ sung

Giữ Ubuntu 22.04 chạy bằng lệnh ở phần bài 1. Mở PowerShell khác và chạy:

```powershell
ssh -o BatchMode=yes -o PreferredAuthentications=publickey root@localhost
```

Chụp ảnh lệnh cùng thông báo `Permission denied (publickey)` bằng `Win+Shift+S`, lưu thành `evidence/04-root-ssh-denied.png`. Đây là SSH local qua WSL2, không phải kết nối tới IP public.

## Bài tập 4: Cấu hình Firewall UFW

Đã cấu hình UFW với mặc định `deny incoming`, `allow outgoing`; chỉ mở `22/tcp` và `8080/tcp`. SSH đang bật port 22 trước khi firewall enable.

Docker published ports có thể đi vòng qua UFW, nên đã thêm rule vào chain `DOCKER-USER` để chặn traffic forwarded tới host port `8081`. Kết quả kiểm tra từ Windows tới IP Ubuntu WSL trong lượt chạy này:

```text
172.25.210.151:8080 -> HTTP 200 (Nginx)
172.25.210.151:8081 -> timeout (blocked)
```

Lưu ý: `localhost:8081` trên Windows vẫn có thể đi qua WSL localhost-forwarding và truy cập được; dùng IP trực tiếp của Ubuntu để quan sát firewall rule. IP WSL có thể đổi sau khi distro khởi động lại. Rule `iptables` trong `DOCKER-USER` hiện là runtime rule và có thể cần tạo lại sau khi Docker/WSL restart.

Các lệnh đã thực thi: [commands-bai-4.txt](commands-bai-4.txt).

Container Docker Desktop có sẵn `butchixanh-shop` cũng dùng Windows port 8080. Container đó đã được tạm dừng để giải phóng cổng, rồi khởi động lại; hiện đã xác nhận trạng thái `Up`. Nginx lab trong Ubuntu được kiểm tra qua IP WSL.

### Ảnh bằng chứng

- Ảnh Nginx 8080 thật đã lưu tại [evidence/06-ufw-8080-nginx.png](evidence/06-ufw-8080-nginx.png).
- Cần chụp `evidence/05-ufw-status.png`: mở Ubuntu 22.04 bằng root và chạy `ufw status verbose` (hoặc `sudo ufw status verbose`).
- Cần chụp `evidence/07-ufw-8081-blocked.png`: từ PowerShell chạy `curl.exe -v --max-time 3 http://172.25.210.151:8081/` và chụp lỗi timeout. Không dùng ảnh trang 8080 làm bằng chứng cho 8081.

Đây là bài thực hành local trên WSL2, không có IP public; phép thử là từ Windows host tới interface IP của Ubuntu WSL.
