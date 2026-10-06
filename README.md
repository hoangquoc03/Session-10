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
