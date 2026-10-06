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
