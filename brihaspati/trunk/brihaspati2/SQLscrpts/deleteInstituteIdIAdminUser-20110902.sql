use brihaspati;

delete from INSTITUTE_ADMIN_USER where NOT EXISTS (select INSTITUTE_ID from INSTITUTE_ADMIN_REGISTRATION where(INSTITUTE_ADMIN_USER.INSTITUTE_ID=INSTITUTE_ADMIN_REGISTRATION.INSTITUTE_ID));
