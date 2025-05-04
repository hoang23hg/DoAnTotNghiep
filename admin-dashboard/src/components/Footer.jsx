const Footer = () => {
    return (
        <footer className="bg-gray-900 text-white py-6 mt-10">
            <div className="container mx-auto px-4 grid md:grid-cols-3 gap-6">
                {/* Thông tin nhóm phát triển */}
                <div>
                    <h3 className="text-lg font-semibold mb-2">Về Chúng Tôi</h3>
                    <p>Nhóm phát triển website quản trị mua sắm online.</p>
                    <p>&copy; 2025 - Bản quyền thuộc về nhóm </p>
                    <p>Việt Hoàng - Đặng Khánh</p>
                </div>

                {/* Thông tin liên hệ */}
                <div>
                    <h3 className="text-lg font-semibold mb-2">Liên Hệ</h3>
                    <p>Email: lvhoang.23.it@gmail.com</p>
                    <p>Điện thoại: 0377077203</p>
                    <p>Địa chỉ: 197 Hoàng Mai - Hà Nội</p>
                </div>

                {/* Bản đồ Google Maps */}
                <div>
                    <h3 className="text-lg font-semibold mb-2">Địa Chỉ</h3>
                    <iframe 
                        className="w-full h-40 rounded-lg"
                        src="https://www.google.com/maps/embed?https://maps.app.goo.gl/F3kFojgymUmt8qQbApb=!1m18!1m12!1m3!1d3919.4841252035067!2d106.70098797587046!3d10.775658989373998!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x31752f3dcf3ddffb%3A0xdeadbeef12345678!2zMTIzIEPGsOG7nW5nIEJpbmggUGjDuiwgVGjDoG5oIFBo4buRLCBUUC5IQ00!5e0!3m2!1sen!2s!4v1700000000000"
                        allowFullScreen
                        loading="lazy"
                    ></iframe>
                </div>
            </div>
        </footer>
    );
};

export default Footer;
