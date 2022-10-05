package com.example.app_locker

import android.annotation.SuppressLint
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.facebook.FacebookSdk
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class MainActivity : AppCompatActivity()
{
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment=supportFragmentManager
            .findFragmentById(R.id.container) as NavHostFragment
        navController=navHostFragment.navController
        printKeyHash()
    }
    @SuppressLint("PackageManagerGetSignatures")
    private fun printKeyHash()
    {
        try
        {
            val info: PackageInfo =packageManager.getPackageInfo("com.example.app_locker",PackageManager.GET_SIGNATURES)
            for (signature in info.signatures)
            {
                val md=MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.e("KEYHASH",Base64.encodeToString(md.digest(),Base64.DEFAULT))
            }
        }
        catch(e:PackageManager.NameNotFoundException)
        {

        }
        catch (e:NoSuchAlgorithmException)
        {

        }

    }
}