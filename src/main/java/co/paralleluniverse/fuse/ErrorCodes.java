package co.paralleluniverse.fuse;

final class ErrorCodes {
    private static final class ErrorCodesBSD implements IErrorCodes {
        @Override public int E2BIG()         { return 7; }
        @Override public int EACCES()        { return 13; }
        @Override public int EADDRINUSE()    { return 48; }
        @Override public int EADDRNOTAVAIL() { return 49; }
        @Override public int EAFNOSUPPORT()  { return 47; }
        @Override public int EAGAIN()        { return 35; }
        @Override public int EALREADY()      { return 37; }
        @Override public int EBADF()         { return 9; }
        @Override public int EBADMSG()       { return 89; }
        @Override public int EBUSY()         { return 16; }
        @Override public int ECANCELED()     { return 85; }
        @Override public int ECHILD()        { return 10; }
        @Override public int ECONNABORTED()  { return 53; }
        @Override public int ECONNREFUSED()  { return 61; }
        @Override public int ECONNRESET()    { return 54; }
        @Override public int EDEADLK()       { return 11; }
        @Override public int EDESTADDRREQ()  { return 39; }
        @Override public int EDOM()          { return 33; }
        @Override public int EDQUOT()        { return 69; }
        @Override public int EEXIST()        { return 17; }
        @Override public int EFAULT()        { return 14; }
        @Override public int EFBIG()         { return 27; }
        @Override public int EHOSTDOWN()     { return 64; }
        @Override public int EHOSTUNREACH()  { return 65; }
        @Override public int EIDRM()         { return 82; }
        @Override public int EILSEQ()        { return 86; }
        @Override public int EINPROGRESS()   { return 36; }
        @Override public int EINTR()         { return 4; }
        @Override public int EINVAL()        { return 22; }
        @Override public int EIO()           { return 5; }
        @Override public int EISCONN()       { return 56; }
        @Override public int EISDIR()        { return 21; }
        @Override public int ELOOP()         { return 62; }
        @Override public int EMFILE()        { return 24; }
        @Override public int EMLINK()        { return 31; }
        @Override public int EMSGSIZE()      { return 40; }
        @Override public int EMULTIHOP()     { return 90; }
        @Override public int ENAMETOOLONG()  { return 63; }
        @Override public int ENETDOWN()      { return 50; }
        @Override public int ENETRESET()     { return 52; }
        @Override public int ENETUNREACH()   { return 51; }
        @Override public int ENFILE()        { return 23; }
        @Override public int ENOBUFS()       { return 55; }
        @Override public int ENODEV()        { return 19; }
        @Override public int ENOENT()        { return 2; }
        @Override public int ENOEXEC()       { return 8; }
        @Override public int ENOLCK()        { return 77; }
        @Override public int ENOLINK()       { return 91; }
        @Override public int ENOMEM()        { return 12; }
        @Override public int ENOMSG()        { return 83; }
        @Override public int ENOPROTOOPT()   { return 42; }
        @Override public int ENOSPC()        { return 28; }
        @Override public int ENOSYS()        { return 78; }
        @Override public int ENOTBLK()       { return 15; }
        @Override public int ENOTCONN()      { return 57; }

        @Override public Integer EADV()         { return null; }
        @Override public Integer EAUTH()        { return 80; }
        @Override public Integer EBADE()        { return null; }
        @Override public Integer EBADFD()       { return null; }
        @Override public Integer EBADR()        { return null; }
        @Override public Integer EBADRPC()      { return 72; }
        @Override public Integer EBADRQC()      { return null; }
        @Override public Integer EBADSLT()      { return null; }
        @Override public Integer EBFONT()       { return null; }
        @Override public Integer ECHRNG()       { return null; }
        @Override public Integer ECOMM()        { return null; }
        @Override public Integer EDEADLOCK()    { return null; }
        @Override public Integer EDOOFUS()      { return 88; }
        @Override public Integer EDOTDOT()      { return null; }
        @Override public Integer EFTYPE()       { return 79; }
        @Override public Integer EISNAM()       { return null; }
        @Override public Integer EKEYEXPIRED()  { return null; }
        @Override public Integer EKEYREJECTED() { return null; }
        @Override public Integer EKEYREVOKED()  { return null; }
        @Override public Integer EL2HLT()       { return null; }
        @Override public Integer EL2NSYNC()     { return null; }
        @Override public Integer EL3HLT()       { return null; }
        @Override public Integer EL3RST()       { return null; }
        @Override public Integer ELAST()        { return 93; }
        @Override public Integer ELIBACC()      { return null; }
        @Override public Integer ELIBBAD()      { return null; }
        @Override public Integer ELIBEXEC()     { return null; }
        @Override public Integer ELIBMAX()      { return null; }
        @Override public Integer ELIBSCN()      { return null; }
        @Override public Integer ELNRNG()       { return null; }
        @Override public Integer EMEDIUMTYPE()  { return null; }
        @Override public Integer ENAVAIL()      { return null; }
        @Override public Integer ENEEDAUTH()    { return 81; }
        @Override public Integer ENOANO()       { return null; }
        @Override public Integer ENOATTR()      { return 87; }
        @Override public Integer ENOCSI()       { return null; }
        @Override public Integer ENODATA()      { return null; }
        @Override public Integer ENOKEY()       { return null; }
        @Override public Integer ENOMEDIUM()    { return null; }
        @Override public Integer ENONET()       { return null; }
        @Override public Integer ENOPKG()       { return null; }
        @Override public Integer ENOSR()        { return null; }
        @Override public Integer ENOSTR()       { return null; }
        @Override public Integer ENOTCAPABLE()  { return 93; }



        @Override
        public int ENOTDIR() {
            return 20;
        }

        @Override
        public int ENOTEMPTY() {
            return 66;
        }

        @Override
        public Integer ENOTNAM() {
            return null;
        }

        @Override
        public Integer ENOTRECOVERABLE() {
            return null;
        }

        @Override
        public int ENOTSOCK() {
            return 38;
        }

        @Override
        public Integer ENOTSUP() {
            return EOPNOTSUPP();
        }

        @Override
        public int ENOTTY() {
            return 25;
        }

        @Override
        public Integer ENOTUNIQ() {
            return null;
        }

        @Override
        public int ENXIO() {
            return 6;
        }

        @Override
        public int EOPNOTSUPP() {
            return 45;
        }

        @Override
        public int EOVERFLOW() {
            return 84;
        }

        @Override
        public Integer EOWNERDEAD() {
            return null;
        }

        @Override
        public int EPERM() {
            return 1;
        }

        @Override
        public int EPFNOSUPPORT() {
            return 46;
        }

        @Override
        public int EPIPE() {
            return 32;
        }

        @Override
        public Integer EPROCLIM() {
            return 67;
        }

        @Override
        public Integer EPROCUNAVAIL() {
            return 76;
        }

        @Override
        public Integer EPROGMISMATCH() {
            return 75;
        }

        @Override
        public Integer EPROGUNAVAIL() {
            return 74;
        }

        @Override
        public int EPROTO() {
            return 92;
        }

        @Override
        public int EPROTONOSUPPORT() {
            return 43;
        }

        @Override
        public int EPROTOTYPE() {
            return 41;
        }

        @Override
        public int ERANGE() {
            return 34;
        }

        @Override
        public Integer EREMCHG() {
            return null;
        }

        @Override
        public int EREMOTE() {
            return 71;
        }

        @Override
        public Integer EREMOTEIO() {
            return null;
        }

        @Override
        public Integer ERESTART() {
            return null;
        }

        @Override
        public int EROFS() {
            return 30;
        }

        @Override
        public Integer ERPCMISMATCH() {
            return 73;
        }

        @Override
        public int ESHUTDOWN() {
            return 58;
        }

        @Override
        public int ESOCKTNOSUPPORT() {
            return 44;
        }

        @Override
        public int ESPIPE() {
            return 29;
        }

        @Override
        public int ESRCH() {
            return 3;
        }

        @Override
        public Integer ESRMNT() {
            return null;
        }

        @Override
        public int ESTALE() {
            return 70;
        }

        @Override
        public Integer ESTRPIPE() {
            return null;
        }

        @Override
        public Integer ETIME() {
            return null;
        }

        @Override
        public int ETIMEDOUT() {
            return 60;
        }

        @Override
        public int ETOOMANYREFS() {
            return 59;
        }

        @Override
        public int ETXTBSY() {
            return 26;
        }

        @Override public Integer EUCLEAN() { return null; }
        @Override public Integer EUNATCH() { return null; }
        @Override public int EUSERS() { return 68; }
        @Override public int EWOULDBLOCK() { return EAGAIN(); }
        @Override public int EXDEV() { return 18; }
        @Override public Integer EXFULL() { return null; }
    }

    private static final class ErrorCodesLinux implements IErrorCodes {
        @Override
        public int E2BIG() {
            return 7;
        }

        @Override
        public int EACCES() {
            return 13;
        }

        @Override
        public int EADDRINUSE() {
            return 98;
        }

        @Override
        public int EADDRNOTAVAIL() {
            return 99;
        }

        @Override
        public Integer EADV() {
            return 68;
        }

        @Override
        public int EAFNOSUPPORT() {
            return 97;
        }

        @Override
        public int EAGAIN() {
            return 11;
        }

        @Override
        public int EALREADY() {
            return 114;
        }

        @Override
        public Integer EAUTH() {
            return null;
        }

        @Override
        public Integer EBADE() {
            return 52;
        }

        @Override
        public int EBADF() {
            return 9;
        }

        @Override
        public Integer EBADFD() {
            return 77;
        }

        @Override
        public int EBADMSG() {
            return 74;
        }

        @Override
        public Integer EBADR() {
            return 53;
        }

        @Override
        public Integer EBADRPC() {
            return null;
        }

        @Override
        public Integer EBADRQC() {
            return 56;
        }

        @Override
        public Integer EBADSLT() {
            return 57;
        }

        @Override
        public Integer EBFONT() {
            return 59;
        }

        @Override
        public int EBUSY() {
            return 16;
        }

        @Override
        public int ECANCELED() {
            return 125;
        }

        @Override
        public int ECHILD() {
            return 10;
        }

        @Override
        public Integer ECHRNG() {
            return 44;
        }

        @Override
        public Integer ECOMM() {
            return 70;
        }

        @Override
        public int ECONNABORTED() {
            return 103;
        }

        @Override
        public int ECONNREFUSED() {
            return 111;
        }

        @Override
        public int ECONNRESET() {
            return 104;
        }

        @Override
        public int EDEADLK() {
            return 35;
        }

        @Override
        public Integer EDEADLOCK() {
            return EDEADLK();
        }

        @Override
        public int EDESTADDRREQ() {
            return 89;
        }

        @Override
        public int EDOM() {
            return 33;
        }

        @Override
        public Integer EDOOFUS() {
            return null;
        }

        @Override
        public Integer EDOTDOT() {
            return 73;
        }

        @Override
        public int EDQUOT() {
            return 122;
        }

        @Override
        public int EEXIST() {
            return 17;
        }

        @Override
        public int EFAULT() {
            return 14;
        }

        @Override
        public int EFBIG() {
            return 27;
        }

        @Override
        public Integer EFTYPE() {
            return null;
        }

        @Override
        public int EHOSTDOWN() {
            return 112;
        }

        @Override
        public int EHOSTUNREACH() {
            return 113;
        }

        @Override
        public int EIDRM() {
            return 43;
        }

        @Override
        public int EILSEQ() {
            return 84;
        }

        @Override
        public int EINPROGRESS() {
            return 115;
        }

        @Override
        public int EINTR() {
            return 4;
        }

        @Override
        public int EINVAL() {
            return 22;
        }

        @Override
        public int EIO() {
            return 5;
        }

        @Override
        public int EISCONN() {
            return 106;
        }

        @Override
        public int EISDIR() {
            return 21;
        }

        @Override
        public Integer EISNAM() {
            return 120;
        }

        @Override
        public Integer EKEYEXPIRED() {
            return 127;
        }

        @Override
        public Integer EKEYREJECTED() {
            return 129;
        }

        @Override
        public Integer EKEYREVOKED() {
            return 128;
        }

        @Override
        public Integer EL2HLT() {
            return 51;
        }

        @Override
        public Integer EL2NSYNC() {
            return 45;
        }

        @Override
        public Integer EL3HLT() {
            return 46;
        }

        @Override
        public Integer EL3RST() {
            return 47;
        }

        @Override
        public Integer ELAST() {
            return null;
        }

        @Override
        public Integer ELIBACC() {
            return 79;
        }

        @Override
        public Integer ELIBBAD() {
            return 80;
        }

        @Override
        public Integer ELIBEXEC() {
            return 83;
        }

        @Override
        public Integer ELIBMAX() {
            return 82;
        }

        @Override
        public Integer ELIBSCN() {
            return 81;
        }

        @Override
        public Integer ELNRNG() {
            return 48;
        }

        @Override
        public int ELOOP() {
            return 40;
        }

        @Override
        public Integer EMEDIUMTYPE() {
            return 124;
        }

        @Override
        public int EMFILE() {
            return 24;
        }

        @Override
        public int EMLINK() {
            return 31;
        }

        @Override
        public int EMSGSIZE() {
            return 90;
        }

        @Override
        public int EMULTIHOP() {
            return 72;
        }

        @Override
        public int ENAMETOOLONG() {
            return 36;
        }

        @Override
        public Integer ENAVAIL() {
            return 119;
        }

        @Override
        public Integer ENEEDAUTH() {
            return null;
        }

        @Override
        public int ENETDOWN() {
            return 100;
        }

        @Override
        public int ENETRESET() {
            return 102;
        }

        @Override
        public int ENETUNREACH() {
            return 101;
        }

        @Override
        public int ENFILE() {
            return 23;
        }

        @Override
        public Integer ENOANO() {
            return 55;
        }

        @Override
        public Integer ENOATTR() {
            return null;
        }

        @Override
        public int ENOBUFS() {
            return 105;
        }

        @Override
        public Integer ENOCSI() {
            return 50;
        }

        @Override
        public Integer ENODATA() {
            return 61;
        }

        @Override
        public int ENODEV() {
            return 19;
        }

        @Override
        public int ENOENT() {
            return 2;
        }

        @Override
        public int ENOEXEC() {
            return 8;
        }

        @Override
        public Integer ENOKEY() {
            return 126;
        }

        @Override
        public int ENOLCK() {
            return 37;
        }

        @Override
        public int ENOLINK() {
            return 67;
        }

        @Override
        public Integer ENOMEDIUM() {
            return 123;
        }

        @Override
        public int ENOMEM() {
            return 12;
        }

        @Override
        public int ENOMSG() {
            return 42;
        }

        @Override
        public Integer ENONET() {
            return 64;
        }

        @Override
        public Integer ENOPKG() {
            return 65;
        }

        @Override
        public int ENOPROTOOPT() {
            return 92;
        }

        @Override
        public int ENOSPC() {
            return 28;
        }

        @Override
        public Integer ENOSR() {
            return 63;
        }

        @Override
        public Integer ENOSTR() {
            return 60;
        }

        @Override
        public int ENOSYS() {
            return 38;
        }

        @Override
        public int ENOTBLK() {
            return 15;
        }

        @Override
        public Integer ENOTCAPABLE() {
            return null;
        }

        @Override
        public int ENOTCONN() {
            return 107;
        }

        @Override
        public int ENOTDIR() {
            return 20;
        }

        @Override
        public int ENOTEMPTY() {
            return 39;
        }

        @Override
        public Integer ENOTNAM() {
            return 118;
        }

        @Override
        public Integer ENOTRECOVERABLE() {
            return 131;
        }

        @Override
        public int ENOTSOCK() {
            return 88;
        }

        @Override
        public Integer ENOTSUP() {
            return null;
        }

        @Override
        public int ENOTTY() {
            return 25;
        }

        @Override
        public Integer ENOTUNIQ() {
            return 76;
        }

        @Override
        public int ENXIO() {
            return 6;
        }

        @Override
        public int EOPNOTSUPP() {
            return 95;
        }

        @Override
        public int EOVERFLOW() {
            return 75;
        }

        @Override
        public Integer EOWNERDEAD() {
            return 130;
        }

        @Override
        public int EPERM() {
            return 1;
        }

        @Override
        public int EPFNOSUPPORT() {
            return 96;
        }

        @Override
        public int EPIPE() {
            return 32;
        }

        @Override
        public Integer EPROCLIM() {
            return null;
        }

        @Override
        public Integer EPROCUNAVAIL() {
            return null;
        }

        @Override
        public Integer EPROGMISMATCH() {
            return null;
        }

        @Override
        public Integer EPROGUNAVAIL() {
            return null;
        }

        @Override
        public int EPROTO() {
            return 71;
        }

        @Override
        public int EPROTONOSUPPORT() {
            return 93;
        }

        @Override
        public int EPROTOTYPE() {
            return 91;
        }

        @Override
        public int ERANGE() {
            return 34;
        }

        @Override
        public Integer EREMCHG() {
            return 78;
        }

        @Override
        public int EREMOTE() {
            return 66;
        }

        @Override
        public Integer EREMOTEIO() {
            return 121;
        }

        @Override
        public Integer ERESTART() {
            return 85;
        }

        @Override
        public int EROFS() {
            return 30;
        }

        @Override
        public Integer ERPCMISMATCH() {
            return null;
        }

        @Override
        public int ESHUTDOWN() {
            return 108;
        }

        @Override
        public int ESOCKTNOSUPPORT() {
            return 94;
        }

        @Override
        public int ESPIPE() {
            return 29;
        }

        @Override
        public int ESRCH() {
            return 3;
        }

        @Override
        public Integer ESRMNT() {
            return 69;
        }

        @Override
        public int ESTALE() {
            return 116;
        }

        @Override
        public Integer ESTRPIPE() {
            return 86;
        }

        @Override
        public Integer ETIME() {
            return 62;
        }

        @Override
        public int ETIMEDOUT() {
            return 110;
        }

        @Override
        public int ETOOMANYREFS() {
            return 109;
        }

        @Override
        public int ETXTBSY() {
            return 26;
        }

        @Override
        public Integer EUCLEAN() {
            return 117;
        }

        @Override
        public Integer EUNATCH() {
            return 49;
        }

        @Override
        public int EUSERS() {
            return 87;
        }

        @Override
        public int EWOULDBLOCK() {
            return EAGAIN();
        }

        @Override
        public int EXDEV() {
            return 18;
        }

        @Override
        public Integer EXFULL() {
            return 54;
        }
    }

    private static interface IErrorCodes {
        int E2BIG();

        int EACCES();

        int EADDRINUSE();

        int EADDRNOTAVAIL();

        Integer EADV();

        int EAFNOSUPPORT();

        int EAGAIN();

        int EALREADY();

        Integer EAUTH();

        Integer EBADE();

        int EBADF();

        Integer EBADFD();

        int EBADMSG();

        Integer EBADR();

        Integer EBADRPC();

        Integer EBADRQC();

        Integer EBADSLT();

        Integer EBFONT();

        int EBUSY();

        int ECANCELED();

        int ECHILD();

        Integer ECHRNG();

        Integer ECOMM();

        int ECONNABORTED();

        int ECONNREFUSED();

        int ECONNRESET();

        int EDEADLK();

        Integer EDEADLOCK();

        int EDESTADDRREQ();

        int EDOM();

        Integer EDOOFUS();

        Integer EDOTDOT();

        int EDQUOT();

        int EEXIST();

        int EFAULT();

        int EFBIG();

        Integer EFTYPE();

        int EHOSTDOWN();

        int EHOSTUNREACH();

        int EIDRM();

        int EILSEQ();

        int EINPROGRESS();

        int EINTR();

        int EINVAL();

        int EIO();

        int EISCONN();

        int EISDIR();

        Integer EISNAM();

        Integer EKEYEXPIRED();

        Integer EKEYREJECTED();

        Integer EKEYREVOKED();

        Integer EL2HLT();

        Integer EL2NSYNC();

        Integer EL3HLT();

        Integer EL3RST();

        Integer ELAST();

        Integer ELIBACC();

        Integer ELIBBAD();

        Integer ELIBEXEC();

        Integer ELIBMAX();

        Integer ELIBSCN();

        Integer ELNRNG();

        int ELOOP();

        Integer EMEDIUMTYPE();

        int EMFILE();

        int EMLINK();

        int EMSGSIZE();

        int EMULTIHOP();

        int ENAMETOOLONG();

        Integer ENAVAIL();

        Integer ENEEDAUTH();

        int ENETDOWN();

        int ENETRESET();

        int ENETUNREACH();

        int ENFILE();

        Integer ENOANO();

        Integer ENOATTR();

        int ENOBUFS();

        Integer ENOCSI();

        Integer ENODATA();

        int ENODEV();

        int ENOENT();

        int ENOEXEC();

        Integer ENOKEY();

        int ENOLCK();

        int ENOLINK();

        Integer ENOMEDIUM();

        int ENOMEM();

        int ENOMSG();

        Integer ENONET();

        Integer ENOPKG();

        int ENOPROTOOPT();

        int ENOSPC();

        Integer ENOSR();

        Integer ENOSTR();

        int ENOSYS();

        int ENOTBLK();

        Integer ENOTCAPABLE();

        int ENOTCONN();

        int ENOTDIR();

        int ENOTEMPTY();

        Integer ENOTNAM();

        Integer ENOTRECOVERABLE();

        int ENOTSOCK();

        Integer ENOTSUP();

        int ENOTTY();

        Integer ENOTUNIQ();

        int ENXIO();

        int EOPNOTSUPP();

        int EOVERFLOW();

        Integer EOWNERDEAD();

        int EPERM();

        int EPFNOSUPPORT();

        int EPIPE();

        Integer EPROCLIM();

        Integer EPROCUNAVAIL();

        Integer EPROGMISMATCH();

        Integer EPROGUNAVAIL();

        int EPROTO();

        int EPROTONOSUPPORT();

        int EPROTOTYPE();

        int ERANGE();

        Integer EREMCHG();

        int EREMOTE();

        Integer EREMOTEIO();

        Integer ERESTART();

        int EROFS();

        Integer ERPCMISMATCH();

        int ESHUTDOWN();

        int ESOCKTNOSUPPORT();

        int ESPIPE();

        int ESRCH();

        Integer ESRMNT();

        int ESTALE();

        Integer ESTRPIPE();

        Integer ETIME();

        int ETIMEDOUT();

        int ETOOMANYREFS();

        int ETXTBSY();

        Integer EUCLEAN();

        Integer EUNATCH();

        int EUSERS();

        int EWOULDBLOCK();

        int EXDEV();

        Integer EXFULL();
    }

    private static IErrorCodes platformErrorCodes = null;

    /**
     * Argument list too long
     */
    public static int E2BIG() {
        return getPlatformErrorCodes().E2BIG();
    }

    /**
     * Permission denied
     */
    public static int EACCES() {
        return getPlatformErrorCodes().EACCES();
    }

    /**
     * Address already in use
     */
    public static int EADDRINUSE() {
        return getPlatformErrorCodes().EADDRINUSE();
    }

    /**
     * Can't assign requested address
     */
    public static int EADDRNOTAVAIL() {
        return getPlatformErrorCodes().EADDRNOTAVAIL();
    }

    /**
     * Advertise error
     *
     * @return null on BSD (not defined)
     */
    public static Integer EADV() {
        return getPlatformErrorCodes().EADV();
    }

    /**
     * Address family not supported by protocol family
     */
    public static int EAFNOSUPPORT() {
        return getPlatformErrorCodes().EAFNOSUPPORT();
    }

    /**
     * Resource temporarily unavailable
     */
    public static int EAGAIN() {
        return getPlatformErrorCodes().EAGAIN();
    }

    /**
     * Operation already in progress
     */
    public static int EALREADY() {
        return getPlatformErrorCodes().EALREADY();
    }

    /**
     * Authentication error
     *
     * @return null on Linux (not defined)
     */
    public static Integer EAUTH() {
        return getPlatformErrorCodes().EAUTH();
    }

    /**
     * Invalid exchange
     *
     * @return null on BSD (not defined)
     */
    public static Integer EBADE() {
        return getPlatformErrorCodes().EBADE();
    }

    /**
     * Bad file descriptor
     */
    public static int EBADF() {
        return getPlatformErrorCodes().EBADF();
    }

    /**
     * File descriptor in bad state
     *
     * @return null on BSD (not defined)
     */
    public static Integer EBADFD() {
        return getPlatformErrorCodes().EBADFD();
    }

    /**
     * Bad message
     */
    public static int EBADMSG() {
        return getPlatformErrorCodes().EBADMSG();
    }

    /**
     * Invalid request descriptor
     *
     * @return null on BSD (not defined)
     */
    public static Integer EBADR() {
        return getPlatformErrorCodes().EBADR();
    }

    /**
     * RPC struct is bad
     *
     * @return null on Linux (not defined)
     */
    public static Integer EBADRPC() {
        return getPlatformErrorCodes().EBADRPC();
    }

    /**
     * Invalid request code
     *
     * @return null on BSD (not defined)
     */
    public static Integer EBADRQC() {
        return getPlatformErrorCodes().EBADRQC();
    }

    /**
     * Invalid slot
     *
     * @return null on BSD (not defined)
     */
    public static Integer EBADSLT() {
        return getPlatformErrorCodes().EBADSLT();
    }

    /**
     * Bad font file format
     *
     * @return null on BSD (not defined)
     */
    public static Integer EBFONT() {
        return getPlatformErrorCodes().EBFONT();
    }

    /**
     * Device busy
     */
    public static int EBUSY() {
        return getPlatformErrorCodes().EBUSY();
    }

    /**
     * Operation canceled
     */
    public static int ECANCELED() {
        return getPlatformErrorCodes().ECANCELED();
    }

    /**
     * No child processes
     */
    public static int ECHILD() {
        return getPlatformErrorCodes().ECHILD();
    }

    /**
     * Channel number out of range
     *
     * @return null on BSD (not defined)
     */
    public static Integer ECHRNG() {
        return getPlatformErrorCodes().ECHRNG();
    }

    /**
     * Communication error on send
     *
     * @return null on BSD (not defined)
     */
    public static Integer ECOMM() {
        return getPlatformErrorCodes().ECOMM();
    }

    /**
     * Software caused connection abort
     */
    public static int ECONNABORTED() {
        return getPlatformErrorCodes().ECONNABORTED();
    }

    /**
     * Connection refused
     */
    public static int ECONNREFUSED() {
        return getPlatformErrorCodes().ECONNREFUSED();
    }

    /**
     * Connection reset by peer
     */
    public static int ECONNRESET() {
        return getPlatformErrorCodes().ECONNRESET();
    }

    /**
     * Resource deadlock avoided
     */
    public static int EDEADLK() {
        return getPlatformErrorCodes().EDEADLK();
    }

    /**
     * Resource deadlock avoided
     *
     * @return null on BSD (not defined)
     */
    public static Integer EDEADLOCK() {
        return getPlatformErrorCodes().EDEADLOCK();
    }

    /**
     * Destination address required
     */
    public static int EDESTADDRREQ() {
        return getPlatformErrorCodes().EDESTADDRREQ();
    }

    /**
     * Numerical argument out of domain
     */
    public static int EDOM() {
        return getPlatformErrorCodes().EDOM();
    }

    /**
     * Programming error
     *
     * @return null on Linux (not defined)
     */
    public static Integer EDOOFUS() {
        return getPlatformErrorCodes().EDOOFUS();
    }

    /**
     * RFS specific error
     *
     * @return null on BSD (not defined)
     */
    public static Integer EDOTDOT() {
        return getPlatformErrorCodes().EDOTDOT();
    }

    /**
     * Disc quota exceeded
     */
    public static int EDQUOT() {
        return getPlatformErrorCodes().EDQUOT();
    }

    /**
     * File exists
     */
    public static int EEXIST() {
        return getPlatformErrorCodes().EEXIST();
    }

    /**
     * Bad address
     */
    public static int EFAULT() {
        return getPlatformErrorCodes().EFAULT();
    }

    /**
     * File too large
     */
    public static int EFBIG() {
        return getPlatformErrorCodes().EFBIG();
    }

    /**
     * Inappropriate file type or format
     *
     * @return null on Linux (not defined)
     */
    public static Integer EFTYPE() {
        return getPlatformErrorCodes().EFTYPE();
    }

    /**
     * Host is down
     */
    public static int EHOSTDOWN() {
        return getPlatformErrorCodes().EHOSTDOWN();
    }

    /**
     * No route to host
     */
    public static int EHOSTUNREACH() {
        return getPlatformErrorCodes().EHOSTUNREACH();
    }

    /**
     * Identifier removed
     */
    public static int EIDRM() {
        return getPlatformErrorCodes().EIDRM();
    }

    /**
     * Illegal byte sequence
     */
    public static int EILSEQ() {
        return getPlatformErrorCodes().EILSEQ();
    }

    /**
     * Operation now in progress
     */
    public static int EINPROGRESS() {
        return getPlatformErrorCodes().EINPROGRESS();
    }

    /**
     * Interrupted system call
     */
    public static int EINTR() {
        return getPlatformErrorCodes().EINTR();
    }

    /**
     * Invalid argument
     */
    public static int EINVAL() {
        return getPlatformErrorCodes().EINVAL();
    }

    /**
     * Input/output error
     */
    public static int EIO() {
        return getPlatformErrorCodes().EIO();
    }

    /**
     * Socket is already connected
     */
    public static int EISCONN() {
        return getPlatformErrorCodes().EISCONN();
    }

    /**
     * Is a directory
     */
    public static int EISDIR() {
        return getPlatformErrorCodes().EISDIR();
    }

    /**
     * Is a named type file
     *
     * @return null on BSD (not defined)
     */
    public static Integer EISNAM() {
        return getPlatformErrorCodes().EISNAM();
    }

    /**
     * Key has expired
     *
     * @return null on BSD (not defined)
     */
    public static Integer EKEYEXPIRED() {
        return getPlatformErrorCodes().EKEYEXPIRED();
    }

    /**
     * Key was rejected by service
     *
     * @return null on BSD (not defined)
     */
    public static Integer EKEYREJECTED() {
        return getPlatformErrorCodes().EKEYREJECTED();
    }

    /**
     * Key has been revoked
     *
     * @return null on BSD (not defined)
     */
    public static Integer EKEYREVOKED() {
        return getPlatformErrorCodes().EKEYREVOKED();
    }

    /**
     * Level 2 halted
     *
     * @return null on BSD (not defined)
     */
    public static Integer EL2HLT() {
        return getPlatformErrorCodes().EL2HLT();
    }

    /**
     * Level 2 not synchronized
     *
     * @return null on BSD (not defined)
     */
    public static Integer EL2NSYNC() {
        return getPlatformErrorCodes().EL2NSYNC();
    }

    /**
     * Level 3 halted
     *
     * @return null on BSD (not defined)
     */
    public static Integer EL3HLT() {
        return getPlatformErrorCodes().EL3HLT();
    }

    /**
     * Level 3 reset
     *
     * @return null on BSD (not defined)
     */
    public static Integer EL3RST() {
        return getPlatformErrorCodes().EL3RST();
    }

    /**
     * Must be equal largest errno
     *
     * @return null on Linux (not defined)
     */
    public static Integer ELAST() {
        return getPlatformErrorCodes().ELAST();
    }

    /**
     * Can not access a needed shared library
     *
     * @return null on BSD (not defined)
     */
    public static Integer ELIBACC() {
        return getPlatformErrorCodes().ELIBACC();
    }

    /**
     * Accessing a corrupted shared library
     *
     * @return null on BSD (not defined)
     */
    public static Integer ELIBBAD() {
        return getPlatformErrorCodes().ELIBBAD();
    }

    /**
     * Cannot exec a shared library directly
     *
     * @return null on BSD (not defined)
     */
    public static Integer ELIBEXEC() {
        return getPlatformErrorCodes().ELIBEXEC();
    }

    /**
     * Attempting to link in too many shared libraries
     *
     * @return null on BSD (not defined)
     */
    public static Integer ELIBMAX() {
        return getPlatformErrorCodes().ELIBMAX();
    }

    /**
     * .lib section in a.out corrupted
     *
     * @return null on BSD (not defined)
     */
    public static Integer ELIBSCN() {
        return getPlatformErrorCodes().ELIBSCN();
    }

    /**
     * Link number out of range
     *
     * @return null on BSD (not defined)
     */
    public static Integer ELNRNG() {
        return getPlatformErrorCodes().ELNRNG();
    }

    /**
     * Too many levels of symbolic links
     */
    public static int ELOOP() {
        return getPlatformErrorCodes().ELOOP();
    }

    /**
     * Wrong medium type
     *
     * @return null on BSD (not defined)
     */
    public static Integer EMEDIUMTYPE() {
        return getPlatformErrorCodes().EMEDIUMTYPE();
    }

    /**
     * Too many open files
     */
    public static int EMFILE() {
        return getPlatformErrorCodes().EMFILE();
    }

    /**
     * Too many links
     */
    public static int EMLINK() {
        return getPlatformErrorCodes().EMLINK();
    }

    /**
     * Message too long
     */
    public static int EMSGSIZE() {
        return getPlatformErrorCodes().EMSGSIZE();
    }

    /**
     * Multihop attempted
     */
    public static int EMULTIHOP() {
        return getPlatformErrorCodes().EMULTIHOP();
    }

    /**
     * File name too long
     */
    public static int ENAMETOOLONG() {
        return getPlatformErrorCodes().ENAMETOOLONG();
    }

    /**
     * No XENIX semaphores available
     *
     * @return null on BSD (not defined)
     */
    public static Integer ENAVAIL() {
        return getPlatformErrorCodes().ENAVAIL();
    }

    /**
     * Need authenticator
     *
     * @return null on Linux (not defined)
     */
    public static Integer ENEEDAUTH() {
        return getPlatformErrorCodes().ENEEDAUTH();
    }

    /**
     * Network is down
     */
    public static int ENETDOWN() {
        return getPlatformErrorCodes().ENETDOWN();
    }

    /**
     * Network dropped connection on reset
     */
    public static int ENETRESET() {
        return getPlatformErrorCodes().ENETRESET();
    }

    /**
     * Network is unreachable
     */
    public static int ENETUNREACH() {
        return getPlatformErrorCodes().ENETUNREACH();
    }

    /**
     * Too many open files in system
     */
    public static int ENFILE() {
        return getPlatformErrorCodes().ENFILE();
    }

    /**
     * No anode
     *
     * @return null on BSD (not defined)
     */
    public static Integer ENOANO() {
        return getPlatformErrorCodes().ENOANO();
    }

    /**
     * Attribute not found
     *
     * @return null on Linux (not defined)
     */
    public static Integer ENOATTR() {
        return getPlatformErrorCodes().ENOATTR();
    }

    /**
     * No buffer space available
     */
    public static int ENOBUFS() {
        return getPlatformErrorCodes().ENOBUFS();
    }

    /**
     * No CSI structure available
     *
     * @return null on BSD (not defined)
     */
    public static Integer ENOCSI() {
        return getPlatformErrorCodes().ENOCSI();
    }

    /**
     * No data available
     *
     * @return null on BSD (not defined)
     */
    public static Integer ENODATA() {
        return getPlatformErrorCodes().ENODATA();
    }

    /**
     * Operation not supported by device
     */
    public static int ENODEV() {
        return getPlatformErrorCodes().ENODEV();
    }

    /**
     * No such file or directory
     */
    public static int ENOENT() {
        return getPlatformErrorCodes().ENOENT();
    }

    /**
     * Exec format error
     */
    public static int ENOEXEC() {
        return getPlatformErrorCodes().ENOEXEC();
    }

    /**
     * Required key not available
     *
     * @return null on BSD (not defined)
     */
    public static Integer ENOKEY() {
        return getPlatformErrorCodes().ENOKEY();
    }

    /**
     * No locks available
     */
    public static int ENOLCK() {
        return getPlatformErrorCodes().ENOLCK();
    }

    /**
     * Link has been severed
     */
    public static int ENOLINK() {
        return getPlatformErrorCodes().ENOLINK();
    }

    /**
     * No medium found
     *
     * @return null on BSD (not defined)
     */
    public static Integer ENOMEDIUM() {
        return getPlatformErrorCodes().ENOMEDIUM();
    }

    /**
     * Cannot allocate memory
     */
    public static int ENOMEM() {
        return getPlatformErrorCodes().ENOMEM();
    }

    /**
     * No message of desired type
     */
    public static int ENOMSG() {
        return getPlatformErrorCodes().ENOMSG();
    }

    /**
     * Machine is not on the network
     *
     * @return null on BSD (not defined)
     */
    public static Integer ENONET() {
        return getPlatformErrorCodes().ENONET();
    }

    /**
     * Package not installed
     *
     * @return null on BSD (not defined)
     */
    public static Integer ENOPKG() {
        return getPlatformErrorCodes().ENOPKG();
    }

    /**
     * Protocol not available
     */
    public static int ENOPROTOOPT() {
        return getPlatformErrorCodes().ENOPROTOOPT();
    }

    /**
     * No space left on device
     */
    public static int ENOSPC() {
        return getPlatformErrorCodes().ENOSPC();
    }

    /**
     * Out of streams resources
     *
     * @return null on BSD (not defined)
     */
    public static Integer ENOSR() {
        return getPlatformErrorCodes().ENOSR();
    }

    /**
     * Device not a stream
     *
     * @return null on BSD (not defined)
     */
    public static Integer ENOSTR() {
        return getPlatformErrorCodes().ENOSTR();
    }

    /**
     * Function not implemented
     */
    public static int ENOSYS() {
        return getPlatformErrorCodes().ENOSYS();
    }

    /**
     * Block device required
     */
    public static int ENOTBLK() {
        return getPlatformErrorCodes().ENOTBLK();
    }

    /**
     * Capabilities insufficient
     *
     * @return null on Linux (not defined)
     */
    public static Integer ENOTCAPABLE() {
        return getPlatformErrorCodes().ENOTCAPABLE();
    }

    /**
     * Socket is not connected
     */
    public static int ENOTCONN() {
        return getPlatformErrorCodes().ENOTCONN();
    }

    /**
     * Not a directory
     */
    public static int ENOTDIR() {
        return getPlatformErrorCodes().ENOTDIR();
    }

    /**
     * Directory not empty
     */
    public static int ENOTEMPTY() {
        return getPlatformErrorCodes().ENOTEMPTY();
    }

    /**
     * Not a XENIX named type file
     *
     * @return null on BSD (not defined)
     */
    public static Integer ENOTNAM() {
        return getPlatformErrorCodes().ENOTNAM();
    }

    /**
     * State not recoverable
     *
     * @return null on BSD (not defined)
     */
    public static Integer ENOTRECOVERABLE() {
        return getPlatformErrorCodes().ENOTRECOVERABLE();
    }

    /**
     * Socket operation on non-socket
     */
    public static int ENOTSOCK() {
        return getPlatformErrorCodes().ENOTSOCK();
    }

    /**
     * Operation not supported
     *
     * @return null on Linux (not defined)
     */
    public static Integer ENOTSUP() {
        return getPlatformErrorCodes().ENOTSUP();
    }

    /**
     * Inappropriate ioctl for device
     */
    public static int ENOTTY() {
        return getPlatformErrorCodes().ENOTTY();
    }

    /**
     * Name not unique on network
     *
     * @return null on BSD (not defined)
     */
    public static Integer ENOTUNIQ() {
        return getPlatformErrorCodes().ENOTUNIQ();
    }

    /**
     * Device not configured
     */
    public static int ENXIO() {
        return getPlatformErrorCodes().ENXIO();
    }

    /**
     * Operation not supported
     */
    public static int EOPNOTSUPP() {
        return getPlatformErrorCodes().EOPNOTSUPP();
    }

    /**
     * Value too large to be stored in data type
     */
    public static int EOVERFLOW() {
        return getPlatformErrorCodes().EOVERFLOW();
    }

    /**
     * Owner died
     *
     * @return null on BSD (not defined)
     */
    public static Integer EOWNERDEAD() {
        return getPlatformErrorCodes().EOWNERDEAD();
    }

    /**
     * Operation not permitted
     */
    public static int EPERM() {
        return getPlatformErrorCodes().EPERM();
    }

    /**
     * Protocol family not supported
     */
    public static int EPFNOSUPPORT() {
        return getPlatformErrorCodes().EPFNOSUPPORT();
    }

    /**
     * Broken pipe
     */
    public static int EPIPE() {
        return getPlatformErrorCodes().EPIPE();
    }

    /**
     * Too many processes
     *
     * @return null on Linux (not defined)
     */
    public static Integer EPROCLIM() {
        return getPlatformErrorCodes().EPROCLIM();
    }

    /**
     * Bad procedure for program
     *
     * @return null on Linux (not defined)
     */
    public static Integer EPROCUNAVAIL() {
        return getPlatformErrorCodes().EPROCUNAVAIL();
    }

    /**
     * Program version wrong
     *
     * @return null on Linux (not defined)
     */
    public static Integer EPROGMISMATCH() {
        return getPlatformErrorCodes().EPROGMISMATCH();
    }

    /**
     * RPC prog. not avail
     *
     * @return null on Linux (not defined)
     */
    public static Integer EPROGUNAVAIL() {
        return getPlatformErrorCodes().EPROGUNAVAIL();
    }

    /**
     * Protocol error
     */
    public static int EPROTO() {
        return getPlatformErrorCodes().EPROTO();
    }

    /**
     * Protocol not supported
     */
    public static int EPROTONOSUPPORT() {
        return getPlatformErrorCodes().EPROTONOSUPPORT();
    }

    /**
     * Protocol wrong type for socket
     */
    public static int EPROTOTYPE() {
        return getPlatformErrorCodes().EPROTOTYPE();
    }

    /**
     * Result too large
     */
    public static int ERANGE() {
        return getPlatformErrorCodes().ERANGE();
    }

    /**
     * Remote address changed
     *
     * @return null on BSD (not defined)
     */
    public static Integer EREMCHG() {
        return getPlatformErrorCodes().EREMCHG();
    }

    /**
     * Too many levels of remote in path
     */
    public static int EREMOTE() {
        return getPlatformErrorCodes().EREMOTE();
    }

    /**
     * Remote I/O error
     *
     * @return null on BSD (not defined)
     */
    public static Integer EREMOTEIO() {
        return getPlatformErrorCodes().EREMOTEIO();
    }

    /**
     * Interrupted system call should be restarted
     *
     * @return null on BSD (not defined)
     */
    public static Integer ERESTART() {
        return getPlatformErrorCodes().ERESTART();
    }

    /**
     * Read-only file system
     */
    public static int EROFS() {
        return getPlatformErrorCodes().EROFS();
    }

    /**
     * RPC version wrong
     *
     * @return null on Linux (not defined)
     */
    public static Integer ERPCMISMATCH() {
        return getPlatformErrorCodes().ERPCMISMATCH();
    }

    /**
     * Can't send after socket shutdown
     */
    public static int ESHUTDOWN() {
        return getPlatformErrorCodes().ESHUTDOWN();
    }

    /**
     * Socket type not supported
     */
    public static int ESOCKTNOSUPPORT() {
        return getPlatformErrorCodes().ESOCKTNOSUPPORT();
    }

    /**
     * Illegal seek
     */
    public static int ESPIPE() {
        return getPlatformErrorCodes().ESPIPE();
    }

    /**
     * No such process
     */
    public static int ESRCH() {
        return getPlatformErrorCodes().ESRCH();
    }

    /**
     * Srmount error
     *
     * @return null on BSD (not defined)
     */
    public static Integer ESRMNT() {
        return getPlatformErrorCodes().ESRMNT();
    }

    /**
     * Stale NFS file handle
     */
    public static int ESTALE() {
        return getPlatformErrorCodes().ESTALE();
    }

    /**
     * Streams pipe error
     *
     * @return null on BSD (not defined)
     */
    public static Integer ESTRPIPE() {
        return getPlatformErrorCodes().ESTRPIPE();
    }

    /**
     * Timer expired
     *
     * @return null on BSD (not defined)
     */
    public static Integer ETIME() {
        return getPlatformErrorCodes().ETIME();
    }

    /**
     * Operation timed out
     */
    public static int ETIMEDOUT() {
        return getPlatformErrorCodes().ETIMEDOUT();
    }

    /**
     * Too many references: can't splice
     */
    public static int ETOOMANYREFS() {
        return getPlatformErrorCodes().ETOOMANYREFS();
    }

    /**
     * Text file busy
     */
    public static int ETXTBSY() {
        return getPlatformErrorCodes().ETXTBSY();
    }

    /**
     * Structure needs cleaning
     *
     * @return null on BSD (not defined)
     */
    public static Integer EUCLEAN() {
        return getPlatformErrorCodes().EUCLEAN();
    }

    /**
     * Protocol driver not attached
     *
     * @return null on BSD (not defined)
     */
    public static Integer EUNATCH() {
        return getPlatformErrorCodes().EUNATCH();
    }

    /**
     * Too many users
     */
    public static int EUSERS() {
        return getPlatformErrorCodes().EUSERS();
    }

    /**
     * Operation would block
     */
    public static int EWOULDBLOCK() {
        return getPlatformErrorCodes().EWOULDBLOCK();
    }

    /**
     * Cross-device link
     */
    public static int EXDEV() {
        return getPlatformErrorCodes().EXDEV();
    }

    /**
     * Exchange full
     *
     * @return null on BSD (not defined)
     */
    public static Integer EXFULL() {
        return getPlatformErrorCodes().EXFULL();
    }

    public static Integer firstNonNull(Integer... errorCodes) {
        for (Integer i : errorCodes) {
            if (i != null) {
                return i;
            }
        }
        return null;
    }

    private static IErrorCodes getPlatformErrorCodes() {
        if (platformErrorCodes == null) {
            switch (Platform.platform()) {
                case FREEBSD:
                case MAC:
                case MAC_MACFUSE:
                    platformErrorCodes = new ErrorCodes.ErrorCodesBSD();
                    break;
                default:
                    platformErrorCodes = new ErrorCodes.ErrorCodesLinux();
            }
        }
        return platformErrorCodes;
    }
}
