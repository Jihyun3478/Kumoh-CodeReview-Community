import React from "react";
import { BsFillTrophyFill } from "react-icons/bs";
import { BiSolidUser } from "react-icons/bi";
import styles from '../styles/main.module.css';

 

const Sidebar = () => {
    return (
        <div className={styles.lanking_wapper}>
            <div className={styles.lanking_title}><BsFillTrophyFill /> TOP 10</div>
            <div className={styles.lanking_unit_wapper}>
                <div className={styles.lanking_unit}><BiSolidUser /> 1등 </div>
                <div className={styles.lanking_unit}><BiSolidUser /> 2등 </div>
                <div className={styles.lanking_unit}><BiSolidUser /> 3등 </div>
                <div className={styles.lanking_unit}><BiSolidUser /> 4등 </div>
                <div className={styles.lanking_unit}><BiSolidUser /> 5등 </div>
                <div className={styles.lanking_unit}><BiSolidUser /> 6등 </div>
                <div className={styles.lanking_unit}><BiSolidUser /> 7등 </div>
                <div className={styles.lanking_unit}><BiSolidUser /> 8등 </div>
                <div className={styles.lanking_unit}><BiSolidUser /> 9등 </div>
                <div className={styles.lanking_unit}><BiSolidUser /> 10등 </div>
            </div>          
        </div>
    );
}

export default Sidebar;